/**
 * Mad-Advertisement
 * Copyright (C) 2011-2013 Thorsten Marx <thmarx@gmx.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.marx_labs.ads.server.utils.listener;




import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.marx_labs.ads.base.api.importer.Importer;
import de.marx_labs.ads.base.api.importer.reader.AdXmlReader;
import de.marx_labs.ads.base.utils.utils.logging.LogWrapper;
import de.marx_labs.ads.common.template.TemplateManager;
import de.marx_labs.ads.common.template.impl.freemarker.FMTemplateManager;
import de.marx_labs.ads.common.util.Properties2;
import de.marx_labs.ads.common.util.Strings;
import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.enums.Mode;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.services.AdTypes;
import de.marx_labs.ads.server.utils.AdServerConstants;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.cluster.ClusterManager;
import de.marx_labs.ads.server.utils.listener.configuration.AdServerModule;
import de.marx_labs.ads.server.utils.runnable.AdDbUpdateTask;
import de.marx_labs.ads.services.geo.IPLocationDB;
import de.marx_labs.ads.services.tracking.TrackingService;

import org.apache.log4j.PropertyConfigurator;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;


/**
 * 
 * @author thorsten
 */
public class StartupPlugIn implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(StartupPlugIn.class);
	
	private Timer timer = new Timer();
	
	private Injector injector = null;
	
	public void contextInitialized(ServletContextEvent event) {
		try {
			// Konfiguration einlesen
			String enviroment = event.getServletContext().getInitParameter("enviroment");
			
			String configDirectory = new File(".").getAbsolutePath(); // event.getServletContext().getInitParameter("configDirectory");
			
			if (System.getProperties().containsKey("mad.home")) {
				configDirectory = System.getProperty("mad.home");
			}
			
			if (!configDirectory.endsWith("/")) {
				configDirectory += "/";
			}
			
			System.setProperty("mad.home", configDirectory);
			
			
			
			configDirectory += "config/";
			
			
			// configure log4j
			
			PropertyConfigurator.configure(Properties2.loadProperties(configDirectory + "log4j.properties"));
			
			
			
			RuntimeContext.setEnviroment(enviroment);
			String path = event.getServletContext().getRealPath("/");
			RuntimeContext.setConfiguration(AdServerConstants.CONFIG.PATHES, AdServerConstants.PATHES.WEB, path);
//			RuntimeContext.getProperties().load(new FileReader(configDirectory + "config.properties"));
			RuntimeContext.setProperties(Properties2.loadProperties(configDirectory + "config.properties"));
			
			injector = Guice.createInjector(new AdServerModule());
			
			// Init event logging
			RuntimeContext.clickLogger = new LogWrapper();
			RuntimeContext.clickLogger.init(RuntimeContext.class, new File(configDirectory + "logger_clicks.properties"));
			RuntimeContext.impressionLogger = new LogWrapper();
			RuntimeContext.impressionLogger.init(RuntimeContext.class, new File(configDirectory + "logger_impression.properties"));
			
			// Banner-Datenbank initialisieren
			logger.info("init bannerDB");
			initBannerDB();
			// Ip-Datenbank initialisieren
			logger.info("init ipDB");
			initIpDB();
			logger.info("init trackService");
			intServices();
			
			logger.info("init banner templates");
			initBannerTemplates(path);
			
			logger.info("init templates");
			initTemplates(path + "/WEB-INF/content/templates/");
			
			RuntimeContext.setImporter(new Importer(RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.BANNER_IMPORT_DIRECOTRY), RuntimeContext.getAdDB()));
			
			TimerTask updateTask = new AdDbUpdateTask();
			updateTask.run();
			timer.scheduleAtFixedRate(updateTask, AdDbUpdateTask.delay, AdDbUpdateTask.period);
			
			RuntimeContext.cacheManager = new DefaultCacheManager(configDirectory + "cluster/infinispan_config.xml");
			RuntimeContext.requestBanners = RuntimeContext.cacheManager.getCache("requestBanners");
			RuntimeContext.requestBanners.addListener(new CacheListener());
			
			String clustermode = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.CLUSTERMODE, "false");
			if (clustermode.equalsIgnoreCase("true")) {
				RuntimeContext.setClusterManager(new ClusterManager());
				RuntimeContext.getClusterManager().init();
			}
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}
	
	public void contextDestroyed(ServletContextEvent event) {
		try {
			RuntimeContext.getAdDB().close();
			RuntimeContext.getIpDB().close();
			RuntimeContext.getTrackService().close();
			RuntimeContext.cacheManager.stop();
			
			timer.cancel();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	private void intServices () throws Exception {
		RuntimeContext.setTrackService(injector.getInstance(TrackingService.class));
	}
	
	private void initTemplates (String path) throws IOException {
		File tdir = new File(path);
		File[] templates = tdir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(".ftl")) {
					return true;
				}
				return false;
			}
		});
		
		TemplateManager tempMan = new FMTemplateManager();
		tempMan.init(path);
		for (File template : templates) {
			String tname = Strings.removeExtension(template.getName()).toLowerCase();
			tempMan.registerTemplate(tname, template.getName());
		}
		
		RuntimeContext.setTemplateManager(tempMan);
	}
	
	private void initBannerTemplates (String path) throws IOException {
//		String templatePath = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.BANNER_TEMPLATE_DIR);
		String templatePath = path + "/WEB-INF/content/templates/banner";
		
		RuntimeContext.getBannerRenderer().init(templatePath);
		
		for (AdType type : AdTypes.getTypes()) {
			RuntimeContext.getBannerRenderer().registerTemplate(type.getName().toLowerCase(), type.getName().toLowerCase()+".ftl");
		}
	}
	
	private void initIpDB () throws Exception {
		
		long before = System.currentTimeMillis();
		
		IPLocationDB db = injector.getInstance(IPLocationDB.class);
		db.open(RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.IPDB_DIR));
		RuntimeContext.setIpDB(db);
		
		RuntimeContext.getIpDB().searchIp("213.83.37.145");
		long after = System.currentTimeMillis();
		logger.debug("finish ipDB: " + (after - before) + "ms");
	}
	
	private void initBannerDB () throws Exception {
		
		long before = System.currentTimeMillis();
		AdDBManager manager = AdDBManager.builder().mode(Mode.LOCAL).build();
		manager.getContext().datadir = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.BANNER_DB_DIRECOTRY);
		RuntimeContext.setManager(manager);
		RuntimeContext.setAdDB(manager.getAdDB());
		RuntimeContext.getAdDB().open();
		String bannerPath = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.BANNER_DATA_DIRECOTRY);
		
		File bdir = new File(bannerPath);
		if (bdir.exists() && bdir.isDirectory()) {
			String[] banners = bdir.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					if (name.endsWith(".xml")) {
						return true;
					}
					return false;
				}
			});
			
			for (String banner : banners) {
				AdDefinition b = AdXmlReader.readBannerDefinition(bannerPath + File.separator + banner);
				RuntimeContext.getAdDB().addBanner(b);
			}
		}
		
		RuntimeContext.getAdDB().reopen();
		
		long after = System.currentTimeMillis();
		logger.debug("finish bannerDB: " + (after - before) + "ms");
	}
}

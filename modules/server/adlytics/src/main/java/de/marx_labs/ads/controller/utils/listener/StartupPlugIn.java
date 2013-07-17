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
package de.marx_labs.ads.controller.utils.listener;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentNavigableMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.Hazelcast;

import de.marx_labs.ads.common.util.Properties2;
import de.marx_labs.ads.controller.utils.RuntimeContext;
import de.marx_labs.ads.db.definition.AdDefinition;

/**
 * 
 * @author thorsten
 */
public class StartupPlugIn implements ServletContextListener {

	private static final Logger logger = LoggerFactory
			.getLogger(StartupPlugIn.class);

	public void contextInitialized(ServletContextEvent event) {
		try {
			// Konfiguration einlesen
			String enviroment = event.getServletContext().getInitParameter(
					"enviroment");

			RuntimeContext.setEnviroment(enviroment);
			
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

			PropertyConfigurator.configure(Properties2
					.loadProperties(configDirectory + "log4j_controller.properties"));

			
			String path = event.getServletContext().getRealPath("/");
			RuntimeContext.setProperties(Properties2
					.loadProperties(configDirectory + "config_controller.properties"));

			DB db = DBMaker
					.newAppendFileDB(
							new File(RuntimeContext.getProperties()
									.getProperty("db.dir")))
					.closeOnJvmShutdown().make();

			// open existing an collection (or create new)
			ConcurrentNavigableMap<String, AdDefinition> persistentMap = db
					.getTreeMap("ads");
			
			RuntimeContext.setDb(db);
			RuntimeContext.setPersistentAds(persistentMap);
			
			RuntimeContext.setHazelcastInstance(Hazelcast.newHazelcastInstance());
			Map<String, AdDefinition> distributedMap = RuntimeContext.getHazelcastInstance().getMap("ads");
			RuntimeContext.setDistributedAds(distributedMap);

			// synchronize Maps
			if (persistentMap.size() != distributedMap.size()) {
				logger.info("distributed and presistent maps are out of sync");
				logger.debug("persistent ads: " + persistentMap.size());
				logger.debug("distributed ads: " + distributedMap.size());
				distributedMap.clear();
				distributedMap.putAll(persistentMap);
			}
			
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		try {
			RuntimeContext.getDb().compact();
			RuntimeContext.getDb().close();
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}

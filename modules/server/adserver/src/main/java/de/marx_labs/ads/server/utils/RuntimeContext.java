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
package de.marx_labs.ads.server.utils;


import java.util.HashMap;
import java.util.Properties;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

import de.marx_labs.ads.base.api.importer.Importer;
import de.marx_labs.ads.base.utils.render.AdRenderer;
import de.marx_labs.ads.base.utils.render.impl.freemarker.FreemarkerAdRenderer;
import de.marx_labs.ads.base.utils.utils.logging.LogWrapper;
import de.marx_labs.ads.common.template.TemplateManager;
import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.db.AdDB;
import de.marx_labs.ads.server.utils.cluster.ClusterManager;
import de.marx_labs.ads.services.geo.IPLocationDB;
import de.marx_labs.ads.services.tracking.TrackingService;


public class RuntimeContext {
	private static HashMap<String, HashMap<String, Object>> configuration = new HashMap<String, HashMap<String,Object>>();
	/**
	 * Properties, die beim starten der Anwendungen geladen werden
	 */
	private static Properties properties = new Properties();
	
	public static LogWrapper clickLogger; // = new LogWrapper(RuntimeContext.class, "logger_clicks.properties");
    public static LogWrapper impressionLogger; // = new LogWrapper(RuntimeContext.class, "logger_impression.properties");
	
	private static AdRenderer bannerRenderer = new FreemarkerAdRenderer();
	
	private static TemplateManager templateManager = null;
	
	
	private static String enviroment = null; 
	
	// Banner-Datenbank
	private static AdDB adDB = null;
	private static AdDBManager manager = null;
	
	private static IPLocationDB ipDB = null;
	
	private static TrackingService trackService = null;
	
	private static Importer importer = null;
	
	

	/*
	 * Der Cache enthält Eine Liste mit allen BannerIDs für einen einzelnen Request, 
	 * dadruch werden doppelte Anzeigen eines Banners auf einer Seite verhindert.
	 * 
	 * Achtung: Diese einfach Implementierung verlangt, dass alle Request die von einer Seite kommen
	 * von dem selben AdServer behandelt werden, da die Daten über die angezeigten Banner nicht
	 * verteilt werden.
	 * In Zukunft könnte man eine Implementierung über den TrackingService anstreben
	 */
//	private static SimpleCache<List<String>> requestBanners = new SimpleCache<List<String>>(5);
//	
//	public static SimpleCache<List<String>> getRequestBanners () {
//		return requestBanners;
//	}
	
	private static ClusterManager clusterManager = null;
	
	
	public static ClusterManager getClusterManager() {
		return clusterManager;
	}


	public static void setClusterManager(ClusterManager clusterManager) {
		RuntimeContext.clusterManager = clusterManager;
	}


	public static Importer getImporter() {
		return importer;
	}


	public static void setImporter(Importer importer) {
		RuntimeContext.importer = importer;
	}

	/*
	 * Neue Implementierung auf basis der Infinispan Cache Implementierung.
	 * Infinispan bietet einen verteilten Cache
	 */
	public static EmbeddedCacheManager cacheManager = null;
	public static Cache<String, Boolean> requestBanners = null;
			
	public static Cache<String, Boolean> getRequestBanners () {
		return requestBanners;
	}
	
	
	public static TemplateManager getTemplateManager() {
		return templateManager;
	}
	public static void setTemplateManager(TemplateManager templateManager) {
		RuntimeContext.templateManager = templateManager;
	}
	public static TrackingService getTrackService() {
		return trackService;
	}
	public static void setTrackService(TrackingService trackService) {
		RuntimeContext.trackService = trackService;
	}
	public static AdRenderer getBannerRenderer() {
		return bannerRenderer;
	}
	public static IPLocationDB getIpDB() {
		return ipDB;
	}
	public static void setIpDB(IPLocationDB ipDB) {
		RuntimeContext.ipDB = ipDB;
	}
	public static final AdDB getAdDB() {
		return adDB;
	}
	public static final void setAdDB(AdDB adDB) {
		RuntimeContext.adDB = adDB;
	}
	
	
	
	public static AdDBManager getManager() {
		return manager;
	}


	public static void setManager(AdDBManager manager) {
		RuntimeContext.manager = manager;
	}


	public static String getEnviroment() {
		return enviroment;
	}
	public static void setEnviroment(String enviroment) {
		RuntimeContext.enviroment = enviroment;
	}

	public static void setProperties (Properties props) {
		properties = props;
	}
	public static Properties getProperties () {
		return properties;
	}
	public static int getIntProperty (String key, int defaultValue) {
		if (properties.containsKey(key)) {
			return Integer.valueOf(properties.getProperty(key));
		}

		return defaultValue;
	}
	
	public static boolean getBooleanProperty (String key, boolean defaultValue) {
		if (properties.containsKey(key)) {
			return Boolean.parseBoolean(properties.getProperty(key));
		}

		return defaultValue;
	}
	
	public static void setConfiguration(String config, String key, Object value) {
		if (!configuration.containsKey(config)) {
			configuration.put(config, new HashMap<String, Object>());
		}
		configuration.get(config).put(key, value);
	}

	public static Object getConfiguration (String config, String key) {
		if (configuration.containsKey(config)) {
			return configuration.get(config).get(key);
		}
		return null;
	}

	
}

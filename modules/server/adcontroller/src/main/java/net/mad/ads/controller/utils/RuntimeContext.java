/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
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
package net.mad.ads.controller.utils;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.mapdb.DB;

import com.hazelcast.core.HazelcastInstance;

import net.mad.ads.common.template.TemplateManager;
import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.definition.AdDefinition;



public class RuntimeContext {
	private static HashMap<String, HashMap<String, Object>> configuration = new HashMap<String, HashMap<String,Object>>();
	/**
	 * Properties, die beim starten der Anwendungen geladen werden
	 */
	private static Properties properties = new Properties();
	
	private static String enviroment;

	private static DB db = null;
	
	private static Map<String, AdDefinition> persistentAds = null;
	
	private static Map<String, AdDefinition> distributedAds = null;

	private static HazelcastInstance hazelcastInstance = null;
	
	
	
	public static HazelcastInstance getHazelcastInstance() {
		return hazelcastInstance;
	}
	public static void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
		RuntimeContext.hazelcastInstance = hazelcastInstance;
	}
	public static DB getDb() {
		return db;
	}
	public static void setDb(DB db) {
		RuntimeContext.db = db;
	}
	public static Map<String, AdDefinition> getPersistentAds() {
		return persistentAds;
	}
	public static void setPersistentAds(Map<String, AdDefinition> persistentAds) {
		RuntimeContext.persistentAds = persistentAds;
	}
	public static Map<String, AdDefinition> getDistributedAds() {
		return distributedAds;
	}
	public static void setDistributedAds(Map<String, AdDefinition> distributedAds) {
		RuntimeContext.distributedAds = distributedAds;
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

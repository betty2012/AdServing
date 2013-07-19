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
package de.marx_labs.ads.adlytics.utils;

import java.util.HashMap;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class RuntimeContext {
	private static HashMap<String, HashMap<String, Object>> configuration = new HashMap<String, HashMap<String, Object>>();
	/**
	 * Properties, die beim starten der Anwendungen geladen werden
	 */
	private static Properties properties = new Properties();

	public static final String ENVIROMENT_DECELOPMENT = "development";
	public static final String ENVIROMENT_TEST = "test";
	public static final String ENVIROMENT_STAGING = "staging";
	public static final String ENVIROMENT_PRODUCTION = "production";

	private static String enviroment;

	private static MongoClient mongo = null;
	private static DB mongoDb = null;
	
	
	
	public static DB db() {
		return mongoDb;
	}

	public static void db(DB mongoDb) {
		RuntimeContext.mongoDb = mongoDb;
	}

	public static MongoClient mongo() {
		return mongo;
	}

	public static void mongo(MongoClient mongo) {
		RuntimeContext.mongo = mongo;
	}

	public static String enviroment() {
		return enviroment;
	}

	public static void enviroment(String enviroment) {
		RuntimeContext.enviroment = enviroment;
	}

	public static void properties(Properties props) {
		properties = props;
	}

	public static Properties properties() {
		return properties;
	}

	public static int getIntProperty(String key, int defaultValue) {
		if (properties.containsKey(key)) {
			return Integer.valueOf(properties.getProperty(key));
		}

		return defaultValue;
	}

	public static boolean getBooleanProperty(String key, boolean defaultValue) {
		if (properties.containsKey(key)) {
			return Boolean.parseBoolean(properties.getProperty(key));
		}

		return defaultValue;
	}

	public static void configuration(String config, String key, Object value) {
		if (!configuration.containsKey(config)) {
			configuration.put(config, new HashMap<String, Object>());
		}
		configuration.get(config).put(key, value);
	}

	public static Object configuration(String config, String key) {
		if (configuration.containsKey(config)) {
			return configuration.get(config).get(key);
		}
		return null;
	}

}

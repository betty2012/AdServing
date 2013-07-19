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
package de.marx_labs.ads.adlytics.utils.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;

import de.marx_labs.ads.adlytics.utils.RuntimeContext;
import de.marx_labs.ads.common.util.Properties2;

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

			RuntimeContext.enviroment(enviroment);
			
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
					.loadProperties(configDirectory + "log4j.properties"));

			
			String path = event.getServletContext().getRealPath("/");
			RuntimeContext.properties(Properties2
					.loadProperties(configDirectory + "config.properties"));

			String host = RuntimeContext.properties().getProperty("mongo.host");
			String db = RuntimeContext.properties().getProperty("mongo.db");
			RuntimeContext.mongo(new MongoClient(host));
			RuntimeContext.db(RuntimeContext.mongo().getDB(db));
			
		} catch (Exception e) {
			logger.error("", e);
			throw new RuntimeException(e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		if (RuntimeContext.mongo() != null) {
			RuntimeContext.mongo().close();
		}
	}
}

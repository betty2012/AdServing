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
package de.marx_labs.ads.base.utils.utils.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.logging.Logger;

import de.marx_labs.ads.base.utils.utils.logging.handler.RollingFileHandler;
import de.marx_labs.ads.base.utils.utils.logging.level.AdLevel;
import de.marx_labs.ads.common.util.Properties2;

public class LogWrapper {
	private Logger logger = null;
	public static final String SPACE = " ";
	public static final String TAB = "\t";
	private static final String END_OF_LINE = System
			.getProperty("line.separator");
	public static final String LOGGER_PROPERTY_FILE = "logger.properties";
	public static final String LOG_FILE_NAME = "log.file.name";
	public static final String LOG_LEVEL = "logger.level";
	public static final String LOG_FILE_SIZE = "logger.file.size";
	public static final String LOG_NUMBER_OF_FILES = "logger.numberof.files";
	public static final String LOGGER_NAME = "logger.name";
	public static final String MESSAGE_FORMAT = "logger.message.format";
	public String className = "";

	public void init(Class clazz, URL logPropertiesFile) {
		init(clazz, logPropertiesFile.getFile());
	}
	
	public void init(Class clazz, String logPropertiesFile) {
		init(clazz, new File(logPropertiesFile));
	}
	
	public void init(Class clazz, File logPropertiesFile) {
//		InputStream is = null;
		try {
//			is = new FileInputStream(logPropertiesFile);
//			Properties prop = new Properties();
//			prop.load(is);
			Properties prop = Properties2.loadProperties(logPropertiesFile);
			if (logger == null) {

				logger = Logger.getLogger(prop.getProperty(LOGGER_NAME));
				String fileName = prop.getProperty(LOG_FILE_NAME);
				// FileHandler fh = new FileHandler(fileName.concat("%g.log"),
				// Integer.parseInt(prop.getProperty(LOG_FILE_SIZE)),
				// Integer.parseInt(prop.getProperty(LOG_NUMBER_OF_FILES)),true);
				// fh.setFormatter(new CustomMessageFormatter(new
				// MessageFormat(prop.getProperty(MESSAGE_FORMAT))));

				RollingFileHandler rfh = new RollingFileHandler(fileName,
						".log");
				rfh.setFormatter(new CustomMessageFormatter(new MessageFormat(
						prop.getProperty(MESSAGE_FORMAT))));

				// logger.setLevel(Level.parse(prop.getProperty(LOG_LEVEL)));
				logger.setLevel(AdLevel.parse(prop.getProperty(LOG_LEVEL)));
				logger.addHandler(rfh);
			}
			this.className = clazz.getName();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public LogWrapper(Class clazz, String propFile) {
		URL logPropertiesFile = this.getClass().getClassLoader()
				.getResource(propFile);
		init(clazz, logPropertiesFile);
	}

	public LogWrapper(Class clazz) {
		URL logPropertiesFile = this.getClass().getClassLoader()
				.getResource(LOGGER_PROPERTY_FILE);
		init(clazz, logPropertiesFile);
	}

	public LogWrapper(Class clazz, URL logPropertiesFile) {
		init(clazz, logPropertiesFile);
	}

	public LogWrapper() {

	}

	public void info(String... strings) {
		logger.info(constructMessage(strings));
	}

	public void click(String... strings) {
		logger.log(AdLevel.CLICK, constructMessage(strings));
	}

	public void impression(String... strings) {
		logger.log(AdLevel.IMPRESSION, constructMessage(strings));
	}

	public void debug(String... strings) {
		logger.fine(constructMessage(strings));
	}

	public void warn(String... strings) {
		logger.warning(constructMessage(strings));

	}

	public void entering(String className, String methodName, Object[] data) {

		logger.entering(className, methodName, data);

	}

	public void entering(String className, String methodName) {

		logger.entering(className, methodName);

	}

	public void entering(String className, String methodName, Object data) {

		logger.entering(className, methodName, data);

	}

	public void exiting(String className, String methodName, Object data) {

		logger.exiting(className, methodName, data);

	}

	public void exiting(String className, String methodName) {

		logger.exiting(className, methodName);

	}

	public void error(Exception trw, String... strings) {
		logger.severe(constructMessage(strings) + constructStackTrace(trw));
	}

	private String constructMessage(String[] strings) {
		StringBuilder sbl = new StringBuilder();
		// sbl.append(SPACE);
		// sbl.append(className);
		if (strings != null && strings.length > 0) {
			for (int count = 0; count < strings.length; count++) {
				sbl.append(SPACE);
				sbl.append(strings[count]);
			}
		}

		return sbl.toString();
	}

	private String constructStackTrace(Exception exe) {
		StringBuilder sbl = new StringBuilder();
		sbl.append(END_OF_LINE);
		sbl.append(exe.toString());
		for (StackTraceElement ste : exe.getStackTrace()) {
			sbl.append(END_OF_LINE);
			sbl.append(TAB);
			sbl.append(ste.toString());
		}

		return sbl.toString();
	}

}

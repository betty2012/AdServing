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
package net.mad.ads.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper for loading property-files
 * 
 * System.properties will be available to use in the files like this
 * 
 * ${user.dir}
 * 
 * @author thmarx
 * 
 */
public class Properties2 {

	private static final Logger logger = LoggerFactory
			.getLogger(Properties2.class);

	static public Properties loadProperties(URL propertiesFile)
			throws IOException {
		return loadProperties(propertiesFile.getFile());
	}

	static public Properties loadProperties(String propertiesFile)
			throws IOException {
		return loadProperties(new File(propertiesFile));
	}

	static public Properties loadProperties (File propertiesFile) throws IOException {
		InputStream is = null;
		is = new FileInputStream(propertiesFile);
		Properties prop = new Properties();
		prop.load(is);
		
		return new XProperties(prop);
	}
}

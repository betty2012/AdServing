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
package de.marx_labs.ads.db.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public final class Version {
	private final static String mainVersion;
	private final static String subVersion;
	private final static String bugfix;
	private final static String dev;
	static {
		mainVersion = ResourceBundle.getBundle("META-INF.version", new Locale("addb")).getString("app.main");
		subVersion = ResourceBundle.getBundle("META-INF.version", new Locale("addb")).getString("app.sub");
		bugfix = ResourceBundle.getBundle("META-INF.version", new Locale("addb")).getString("app.bugfix");
		dev = ResourceBundle.getBundle("META-INF.version", new Locale("addb")).getString("app.dev");
	}
	
	private Version () {}
	
	public static void main (String [] args ) {
		System.out.println(Version.mainVersion);
		System.out.println(Version.subVersion);
		System.out.println(Version.bugfix);
		System.out.println(Version.dev);
		
		System.out.println(Version.getVersion());
	}
	
	public static String getVersion () {
		StringBuilder version = new StringBuilder().append(mainVersion).append(".").append(subVersion);
		
		if (bugfix != null && !bugfix.trim().equals("")) {
			version.append(" (bugfix ");
			version.append(bugfix);
			version.append(")");
		}
		if (dev != null && !dev.trim().equals("")) {
			version.append(" (development ");
			version.append(dev);
			version.append(")");
		}
		
		return version.toString();
	}
}

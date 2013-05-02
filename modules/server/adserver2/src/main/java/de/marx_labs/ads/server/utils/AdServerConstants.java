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

public abstract class AdServerConstants {

	
	public final class PATHES {
		public static final String WEB = "web";
	}
	
	public final class CONFIG {
		public static final String PATHES = "pathes";
		
		public final class PROPERTIES {
			public static final String BANNER_IMPORT_DIRECOTRY = "banner.import.dir";
			public static final String BANNER_DATA_DIRECOTRY = "banner.data.dir";
			public static final String BANNER_DB_DIRECOTRY = "banner.db.dir";
			public static final String CLICK_URL = "click.url";
			public static final String STATIC_URL = "static.url";
			public static final String IPDB_DIR = "ipdb.dir";
			public static final String TRACK_DIR = "track.dir";
			public static final String BANNER_TEMPLATE_DIR = "banner.template.dir";
			
			public static final String ADSERVER_URL = "adserver.url";
			public static final String ADSERVER_SELECT_URL = "adserver.select.url";
			
			public static final String SERVICES_TRACKING_CASSANDRA_HOST = "services.tracking.cassandra.host";
			public static final String SERVICES_TRACKING_CASSANDRA_CLUSTER = "services.tracking.cassandra.cluster";			
			
			public static final String COOKIE_DOMAIN = "cookie.domain";
			
			public static final String TRACKINGSERVICE_CLASS = "tracking.service.class";
			public static final String IPLOCATIONSERVICE_CLASS = "iplocation.service.class";
			
			public static final String CLUSTERMODE = "clustermode";
		}
	}
	
	public final class Cookie {
		public static final String USERID = "userid";
	}
}

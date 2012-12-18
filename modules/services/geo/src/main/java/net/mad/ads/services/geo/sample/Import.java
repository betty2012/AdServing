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
package net.mad.ads.services.geo.sample;

import net.mad.ads.services.geo.IpinfoLocationDB;



public class Import {
	public static void main(String[] args) throws Exception {

		IpinfoLocationDB readerhsql = new IpinfoLocationDB();
		readerhsql.open("data/geo/db/ipinfo");

		long before = System.currentTimeMillis();
		System.out.println("start import ipinfodb");
		readerhsql.importCountry("data/geo/imp/IP2LOCATION-LITE-DB11.CSV");
		long after = System.currentTimeMillis();

		System.out.println("end import after: " + (after - before) + "ms");

		readerhsql.close();
	}
}

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
package de.marx_labs.ads.services.geo.sample;

import de.marx_labs.ads.services.geo.IPLocationDB;
import de.marx_labs.ads.services.geo.MaxmindIpLocationDB;




public class MaxmindImport {
	public static void main(String[] args) throws Exception {

		IPLocationDB readerhsql = new MaxmindIpLocationDB();
		readerhsql.open("data/geo/maxmind/ipinfo");

		long before = System.currentTimeMillis();
		System.out.println("start import maxmind");
		readerhsql.importCountry("data/geo/imp/");
		long after = System.currentTimeMillis();

		System.out.println("end import after: " + (after - before) + "ms");

		readerhsql.close();
	}
}

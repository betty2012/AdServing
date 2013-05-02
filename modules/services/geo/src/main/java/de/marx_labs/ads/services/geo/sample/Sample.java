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

import de.marx_labs.ads.services.geo.IpinfoLocationDB;
import de.marx_labs.ads.services.geo.Location;




public class Sample {
	
public static void main(String[] args) throws Exception {
		
		IpinfoLocationDB readerhsql = new IpinfoLocationDB();
		readerhsql.open("data/geo/db/ipinfo");
		
		long before = System.currentTimeMillis();
		Location loc = readerhsql.searchIp("66.211.160.129");
		System.out.println("1: " + loc.getCountry() + " - " + loc.getRegionName() + " - " + loc.getCity());
		long after = System.currentTimeMillis();
		System.out.println((after - before) + "ms");
		
		before = System.currentTimeMillis();
		loc = readerhsql.searchIp("213.83.37.145");
		System.out.println("2: " + loc.getCountry() + " - " + loc.getRegionName() + " - " + loc.getCity());
		after = System.currentTimeMillis();
		System.out.println((after - before) + "ms");
		
		before = System.currentTimeMillis();
		loc = readerhsql.searchIp("88.153.215.174");
		System.out.println("3: " + loc.getCountry() + " - " + loc.getRegionName() + " - " + loc.getCity());
		after = System.currentTimeMillis();
		System.out.println((after - before) + "ms");
		
		
		int c = 10;
		int comp = 0;
		for (int i = 0; i < c; i++) {
			before = System.currentTimeMillis();
			loc = readerhsql.searchIp("213.83.37.145");
//			System.out.println(loc.getCountry() + " - " + loc.getRegionName() + " - " + loc.getCity());
			after = System.currentTimeMillis();
			long dur = (after - before);
//			System.out.println(dur + "ms");
			comp += dur;
		}
		System.out.println("durchschnitt: " + (comp));
		System.out.println("durchschnitt: " + (comp / c));
		
		readerhsql.close();
	}
}

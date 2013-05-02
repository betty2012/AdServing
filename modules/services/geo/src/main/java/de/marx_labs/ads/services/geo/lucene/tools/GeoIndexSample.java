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
package de.marx_labs.ads.services.geo.lucene.tools;

import de.marx_labs.ads.services.geo.Location;
import de.marx_labs.ads.services.geo.lucene.GeoIpIndex;




public class GeoIndexSample {
public static void main (String [] args) throws Exception {
		
		GeoIpIndex geoIndex = new GeoIpIndex("data/geo/index/");
		geoIndex.open();
		
		long before = System.currentTimeMillis();
		Location loc = geoIndex.searchIp("66.211.160.129");
		System.out.println("1: " + loc.getCountry() + " - " + loc.getRegionName() + " - " + loc.getCity());
		long after = System.currentTimeMillis();
		System.out.println((after - before) + "ms");
		
		before = System.currentTimeMillis();
		loc = geoIndex.searchIp("213.83.37.145");
		System.out.println("2: " + loc.getCountry() + " - " + loc.getRegionName() + " - " + loc.getCity());
		after = System.currentTimeMillis();
		System.out.println((after - before) + "ms");
		
		before = System.currentTimeMillis();
		loc = geoIndex.searchIp("88.153.215.174");
		System.out.println("3: " + loc.getCountry() + " - " + loc.getRegionName() + " - " + loc.getCity());
		after = System.currentTimeMillis();
		System.out.println((after - before) + "ms");
		
		
		int c = 10;
		int comp = 0;
		for (int i = 0; i < c; i++) {
			before = System.currentTimeMillis();
			loc = geoIndex.searchIp("213.83.37.145");
//			System.out.println(loc.getCountry() + " - " + loc.getRegionName() + " - " + loc.getCity());
			after = System.currentTimeMillis();
			long dur = (after - before);
//			System.out.println(dur + "ms");
			comp += dur;
		}
		System.out.println("durchschnitt: " + (comp));
		System.out.println("durchschnitt: " + (comp / c));
		
		
		
		geoIndex.close();
	}
}

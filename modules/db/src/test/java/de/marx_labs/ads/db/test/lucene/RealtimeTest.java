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
package de.marx_labs.ads.db.test.lucene;


import de.marx_labs.ads.common.benchmark.StopWatch;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.CountryConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.Country;
import de.marx_labs.ads.db.model.format.impl.FullBannerAdFormat;

import org.junit.Test;


public class RealtimeTest extends AdDBTestCase {
	
	@Test
	public void testRealtime () throws Exception {
		System.out.println("Realtime");
		
		db.open();
		StopWatch sw = new StopWatch();
		sw.start();
		for (int i = 0; i < 500; i++) {
			AdDefinition b = new ImageAdDefinition();
			b.setId("" + (i+1));
			CountryConditionDefinition sdef = new CountryConditionDefinition();
			sdef.addCountry(new Country("DE"));
			b.addConditionDefinition(ConditionDefinitions.COUNTRY, sdef);
			b.setFormat(new FullBannerAdFormat());
			db.addBanner(b);
		}
		
		sw.stop();
		System.out.println(sw.getElapsedTime() + "ms");
		
		
		assertEquals(0, db.size());
		
		sw = sw.reset();
		sw.start();
		
		db.reopen();
		
		sw.stop();
		System.out.println(sw.getElapsedTime() + "ms");
		
		
		assertEquals(db.size(), 500);
		
		db.deleteBanner("5");
		
		assertEquals(db.size(), 500);
		
		db.reopen();
		
		assertEquals(db.size(), 499);
		
		
	}
}

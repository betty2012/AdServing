/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.mad.ads.db.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.mad.ads.common.benchmark.StopWatch;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.CountryConditionDefinition;
import net.mad.ads.db.definition.condition.StateConditionDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.Country;
import net.mad.ads.db.model.format.impl.FullBannerAdFormat;



import junit.framework.TestCase;


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
		
		
		assertEquals(db.size(), 0);
		
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

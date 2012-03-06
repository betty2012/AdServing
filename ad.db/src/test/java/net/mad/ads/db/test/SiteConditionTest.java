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

import net.mad.ads.db.condition.impl.ExcludeSiteCondition;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.CountryConditionDefinition;
import net.mad.ads.db.definition.condition.ExcludeSiteConditionDefinition;
import net.mad.ads.db.definition.condition.SiteConditionDefinition;
import net.mad.ads.db.definition.condition.StateConditionDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.AdFormat;
import net.mad.ads.db.enums.AdType;
import net.mad.ads.db.enums.ConditionDefinitions;


import junit.framework.TestCase;


public class SiteConditionTest extends TestCase {
	
	@Test
	public void testSiteCondition () throws Exception {
		System.out.println("SiteCondition");
		
		AdDB db = new AdDB();
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		SiteConditionDefinition sdef = new SiteConditionDefinition();
		sdef.addSite("10");
		b.addConditionDefinition(ConditionDefinitions.SITE, sdef);
		b.setFormat(AdFormat.FULL_BANNER);
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("2");
		sdef = new SiteConditionDefinition();
		sdef.addSite("11");
		b.addConditionDefinition(ConditionDefinitions.SITE, sdef);
		ExcludeSiteConditionDefinition edef = new ExcludeSiteConditionDefinition();
		edef.addSite("10");
		b.addConditionDefinition(ConditionDefinitions.EXCLUDE_SITE, edef);
		
		b.setFormat(AdFormat.FULL_BANNER);
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(AdFormat.FULL_BANNER);
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdType.IMAGE);
		request.setTypes(types);
		request.setSite("1");
		
		List<AdDefinition> result = db.search(request);
		assertTrue(result.isEmpty());
		
		request.setSite("10");
		result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("1"));
		
		
		request.setSite("11");
		result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("2"));

		request.setSite("12");
		result = db.search(request);
		assertEquals(0, result.size());
		
		AdDefinition b1 = new ImageAdDefinition();
		b1.setId("3");
		b1.setFormat(AdFormat.FULL_BANNER);
		db.addBanner(b1);
		db.reopen();
		
		System.out.println(db.size());
		
		request.setSite("12");
		result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("3"));
		
		db.close();
	}
}

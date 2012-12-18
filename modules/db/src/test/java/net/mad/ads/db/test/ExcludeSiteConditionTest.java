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
package net.mad.ads.db.test;

import java.util.ArrayList;
import java.util.List;

import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.ExcludeSiteConditionDefinition;
import net.mad.ads.db.definition.condition.SiteConditionDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.format.impl.FullBannerAdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdTypes;

import org.junit.Test;


public class ExcludeSiteConditionTest extends AdDBTestCase {
	
	@Test
	public void testSiteCondition () throws Exception {
		System.out.println("ExcludeSiteCondition");
		
		db.open();
		
		AdDefinition b = getAd(new String [] {"10"}, "1", new String [] {"11"});
		db.addBanner(b);
		
		b = getAd(new String [] {"11"}, "2");
		db.addBanner(b);
		
		b = getAd(new String [] {}, "3", new String [] {"10", "11"});
		db.addBanner(b);
		
		db.reopen();
		
		assertEquals(3, db.size());
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		request.setSite("1");
		
		List<AdDefinition> result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("3"));
		
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
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("3"));
		
		
	}
	
	private static AdDefinition getAd (String []sites, String id, String...exclude) {
		AdDefinition b = new ImageAdDefinition();
		b.setId(id);
		SiteConditionDefinition sdef = new SiteConditionDefinition();
		for (String site : sites) {
			sdef.addSite(site);
		}
		b.addConditionDefinition(ConditionDefinitions.SITE, sdef);
		b.setFormat(new FullBannerAdFormat());
		
		if (exclude != null) {
			ExcludeSiteConditionDefinition edef = new ExcludeSiteConditionDefinition();
			for (String ex : exclude) {
				edef.addSite(ex);
			}
			b.addConditionDefinition(ConditionDefinitions.EXCLUDE_SITE, edef);
		}
		
		return b;
	}
}

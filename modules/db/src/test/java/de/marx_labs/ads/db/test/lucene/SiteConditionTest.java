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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.ExcludeSiteConditionDefinition;
import de.marx_labs.ads.db.definition.condition.SiteConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.format.impl.FullBannerAdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdTypes;


public class SiteConditionTest extends AdDBTestCase {
	
	@Test
	public void testSiteCondition () throws Exception {
		System.out.println("SiteCondition");
		
		
		
		db.open();
		
		AdDefinition b = getAd(new String [] {"10"}, "1");
		db.addBanner(b);
		
		b = getAd(new String [] {"11"}, "2");
		db.addBanner(b);
		
		db.reopen();
		
		System.out.println(db.size());
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.formats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.types(types);
		request.site("1");
		
		List<AdDefinition> result = db.search(request);
		assertTrue(result.isEmpty());
		
		request.site("10");
		result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("1"));
		
		
		request.site("11");
		result = db.search(request);
		assertEquals(1, result.size());
		assertTrue(result.get(0).getId().equals("2"));

		request.site("12");
		result = db.search(request);
		assertEquals(0, result.size());
		
		b = getAd(new String [] {"12"}, "3");
		db.addBanner(b);
		
		
		System.out.println(db.size());
		db.reopen();
		System.out.println(db.size());
		
		request = new AdRequest();
		request.formats(formats);
		request.types(types);
		request.site("12");
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

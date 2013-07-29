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
package de.marx_labs.ads.db.test.mongo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.FlashAdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdFormats;
import de.marx_labs.ads.db.services.AdTypes;


public class DefaultAdTest extends AdDBTestCase {
	
	@Test
	public void testStateCondition () throws Exception {
		System.out.println("StateCondition");
		
		
		
		db.open();
		
		ImageAdDefinition b = new ImageAdDefinition();
		b.setFormat(AdFormats.forCompoundName("468x60"));
		b.setId("1");
		b.setDefaultAd(true);
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setFormat(AdFormats.forCompoundName("468x60"));
		b.setId("2");
		b.setDefaultAd(false);
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(FlashAdType.TYPE));
		request.types(types);
		
		List<AdDefinition> result = db.search(request);
		assertTrue(result.isEmpty());
		
		request = new AdRequest();
		request.formats().add(AdFormats.forCompoundName("468x60"));
		request.defaultAd(false);
		types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.types(types);
		
		result = db.search(request);
		assertEquals(1, result.size());
		assertEquals("2", result.get(0).getId());
		
		request = new AdRequest();
		request.formats().add(AdFormats.forCompoundName("468x60"));
		request.defaultAd(true);
		types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.types(types);
		
		result = db.search(request);
		assertEquals(1, result.size());
		assertEquals("1", result.get(0).getId());
		
		
	}
}

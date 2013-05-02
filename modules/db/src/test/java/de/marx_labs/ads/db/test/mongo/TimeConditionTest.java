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

import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.TimeConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.format.impl.FullBannerAdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdTypes;

import org.junit.Test;


public class TimeConditionTest extends AdDBTestCase {
	
	@Test
	public void testTimeCondition () throws Exception {
		System.out.println("TimeCondition");
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		TimeConditionDefinition sdef = new TimeConditionDefinition();
		sdef.addPeriod("0800", "1000");
		sdef.addPeriod("1800", "2000");
		b.addConditionDefinition(ConditionDefinitions.TIME, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		
		request.setTime("0730");
		List<AdDefinition> result = db.search(request);
		assertEquals(0, result.size());
		
		request.setTime("0800");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.setTime("0900");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.setTime("1000");
		result = db.search(request);
		assertEquals(1, result.size());
		
		
		request.setTime("1001");
		result = db.search(request);
		assertEquals(0, result.size());
		
		request.setTime("1759");
		result = db.search(request);
		assertEquals(0, result.size());
		
		request.setTime("1800");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.setTime("1900");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.setTime("2000");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.setTime("2001");
		result = db.search(request);
		assertEquals(0, result.size());
		
	}
}

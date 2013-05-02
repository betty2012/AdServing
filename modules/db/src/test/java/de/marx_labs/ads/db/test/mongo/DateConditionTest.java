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
import de.marx_labs.ads.db.definition.condition.DateConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.format.impl.FullBannerAdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdTypes;

import org.junit.Test;


public class DateConditionTest extends AdDBTestCase {
	
	@Test
	public void testDateCondition () throws Exception {
		System.out.println("DateCondition");
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		DateConditionDefinition sdef = new DateConditionDefinition();
		sdef.addPeriod("20110215", "20110220");
		sdef.addPeriod("20110315", "20110320");
		b.addConditionDefinition(ConditionDefinitions.DATE, sdef);
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
		
		request.setDate("20110214");
		List<AdDefinition> result = db.search(request);
		assertEquals(0, result.size());
		
		request.setDate("20110215");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.setDate("20110220");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.setDate("20110221");
		result = db.search(request);
		assertEquals(0, result.size());
		
		request.setDate("20110314");
		result = db.search(request);
		assertEquals(0, result.size());
		
		request.setDate("20110315");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.setDate("20110320");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.setDate("20110321");
		result = db.search(request);
		assertEquals(0, result.size());
	}
}

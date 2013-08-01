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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.ValidFromToConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.format.impl.FullBannerAdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdTypes;


public class ValidFromToConditionTest extends AdDBTestCase {
	
	@Test
	public void testTimeCondition () throws Exception {
		System.out.println("TimeCondition");
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		ValidFromToConditionDefinition sdef = new ValidFromToConditionDefinition();
		sdef.setPeriod("201308010800", "201308021300");
		b.addConditionDefinition(ConditionDefinitions.VALIDFROMTO, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.formats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.types(types);
		
		request.date("20130801");
		request.time("0730");
		List<AdDefinition> result = db.search(request);
		assertEquals(0, result.size());
		
		request.date("20130801");
		request.time("0800");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.date("20130802");
		request.time("1000");
		result = db.search(request);
		assertEquals(1, result.size());
		
		request.date("20130802");
		request.time("1301");
		result = db.search(request);
		assertEquals(0, result.size());
		
	}
}

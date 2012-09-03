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

import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.DateConditionDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.format.impl.FullBannerAdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdTypes;

import org.junit.Test;


public class DateConditionTest extends AdDBTestCase {
	
	@Test
	public void testDateCondition () throws Exception {
		System.out.println("CountryCondition");
		
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

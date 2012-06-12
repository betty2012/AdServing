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

import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.CountryConditionDefinition;
import net.mad.ads.db.definition.condition.DayConditionDefinition;
import net.mad.ads.db.definition.condition.StateConditionDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.enums.Day;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.format.impl.FullBannerAdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdTypes;


import junit.framework.TestCase;


public class DayConditionTest extends AdDBTestCase {
	
	@Test
	public void testDayCondition () throws Exception {
		System.out.println("DayCondition");
		
		
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		DayConditionDefinition sdef = new DayConditionDefinition();
		sdef.addDay(Day.Monday);
		sdef.addDay(Day.Wednesday);
		b.addConditionDefinition(ConditionDefinitions.DAY, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("2");
		sdef = new DayConditionDefinition();
		sdef.addDay(Day.ALL);
		b.addConditionDefinition(ConditionDefinitions.DAY, sdef);
		b.setFormat(new FullBannerAdFormat());
		db.addBanner(b);
		
		db.reopen();
		System.out.println(db.size());
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		request.setDay(Day.Tuesday);
		
		List<AdDefinition> result = db.search(request);
		assertEquals(1, result.size());
		
		request.setDay(Day.Monday);
		result = db.search(request);
		assertEquals(2, result.size());
		
		request.setDay(Day.Wednesday);
		result = db.search(request);
		assertEquals(2, result.size());
		
		request.setDay(Day.Sunday);
		result = db.search(request);
		System.out.println("size= " + result.size());
		System.out.println("id= " + result.get(0).getId());
		assertEquals(1, result.size());
		
	}
}

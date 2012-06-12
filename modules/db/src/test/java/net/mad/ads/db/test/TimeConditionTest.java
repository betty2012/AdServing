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

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.CountryConditionDefinition;
import net.mad.ads.db.definition.condition.DateConditionDefinition;
import net.mad.ads.db.definition.condition.StateConditionDefinition;
import net.mad.ads.db.definition.condition.TimeConditionDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.format.impl.FullBannerAdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.ImageAdType;
import net.mad.ads.db.services.AdTypes;

import junit.framework.TestCase;


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

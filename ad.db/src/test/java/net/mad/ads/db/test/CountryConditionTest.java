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
import net.mad.ads.db.definition.condition.StateConditionDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.AdFormat;
import net.mad.ads.db.enums.AdType;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.Country;
import net.mad.ads.db.model.State;

import junit.framework.TestCase;


public class CountryConditionTest extends TestCase {
	
	@Test
	public void testStateCondition () throws Exception {
		System.out.println("CountryCondition");
		
		AdDB db = new AdDB();
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		CountryConditionDefinition sdef = new CountryConditionDefinition();
		sdef.addCountry(new Country("DE"));
		b.addConditionDefinition(ConditionDefinitions.COUNTRY, sdef);
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
		request.setCountry(new Country("AT"));
		
		List<AdDefinition> result = db.search(request);
		assertTrue(result.isEmpty());
		
		request.setCountry(new Country("DE"));
		result = db.search(request);
		assertEquals(result.size(), 1);
		
		request.setCountry(Country.ALL);
		result = db.search(request);
		assertEquals(result.size(), 1);
		
		db.close();
	}
}

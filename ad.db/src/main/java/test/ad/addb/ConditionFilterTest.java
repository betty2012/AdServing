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
package test.ad.addb;

import java.util.ArrayList;
import java.util.List;

import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.Keyword;
import net.mad.ads.db.definition.condition.CountryConditionDefinition;
import net.mad.ads.db.definition.condition.DistanceConditionDefinition;
import net.mad.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.Country;
import net.mad.ads.db.enums.Day;
import net.mad.ads.db.model.State;
import net.mad.ads.db.model.format.AdFormat;
import net.mad.ads.db.model.format.impl.FullBannerAdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.ExternAdType;
import net.mad.ads.db.services.AdTypes;
import net.mad.ads.db.utils.geo.GeoLocation;

public class ConditionFilterTest {
	public ConditionFilterTest () {
		
	}
	
	public void doGeoLocationTest () throws Exception {
		System.out.println("geo test");
		AdDBManager manager = AdDBManager.newInstance();
		
		manager.getAdDB().open();
		
		AdDefinition b = new ExternAdDefinition();
		b.setId("1");
		
		CountryConditionDefinition cdef = new CountryConditionDefinition();
		cdef.addCountry(new Country("DE"));
		b.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);
		
		b.setFormat(new FullBannerAdFormat());
		GeoLocation gl = new GeoLocation(51.4844, 7.2188);
		DistanceConditionDefinition dcdef = new DistanceConditionDefinition();
		dcdef.setGeoLocation(gl);
		dcdef.setGeoRadius(5);
		b.addConditionDefinition(ConditionDefinitions.DISTANCE, dcdef);
		
		manager.getAdDB().addBanner(b);
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ExternAdType.TYPE));
		request.setTypes(types);
		
		gl = new GeoLocation(51.4863, 7.180);
		request.setGeoLocation(gl);
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		System.out.println(result.size());
		
		
		gl = new GeoLocation(51.4857, 7.0958);
		request.setGeoLocation(gl);
		result = manager.getAdDB().search(request);
		System.out.println(result.size());
		
		
		manager.getAdDB().close();
	}
	
	public static void main (String [] args) throws Exception {
		ConditionFilterTest st = new ConditionFilterTest();
		
		st.doGeoLocationTest();
	}
}

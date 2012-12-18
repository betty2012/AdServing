/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
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
package test.ad.addb;

import java.util.ArrayList;
import java.util.List;

import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.CountryConditionDefinition;
import net.mad.ads.db.definition.condition.DistanceConditionDefinition;
import net.mad.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.Country;
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
		AdDBManager manager = AdDBManager.builder().build();
		
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

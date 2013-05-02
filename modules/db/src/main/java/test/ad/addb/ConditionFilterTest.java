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
package test.ad.addb;

import java.util.ArrayList;
import java.util.List;

import de.marx_labs.ads.db.AdDBManager;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.CountryConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DistanceConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.Country;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.format.impl.FullBannerAdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.ExternAdType;
import de.marx_labs.ads.db.services.AdTypes;
import de.marx_labs.ads.db.utils.geo.GeoLocation;

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

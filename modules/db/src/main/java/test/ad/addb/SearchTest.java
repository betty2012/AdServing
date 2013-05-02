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
import de.marx_labs.ads.db.definition.Keyword;
import de.marx_labs.ads.db.definition.condition.CountryConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DateConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DayConditionDefinition;
import de.marx_labs.ads.db.definition.condition.KeywordConditionDefinition;
import de.marx_labs.ads.db.definition.condition.SiteConditionDefinition;
import de.marx_labs.ads.db.definition.condition.StateConditionDefinition;
import de.marx_labs.ads.db.definition.condition.TimeConditionDefinition;
import de.marx_labs.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.enums.Day;
import de.marx_labs.ads.db.model.Country;
import de.marx_labs.ads.db.model.State;
import de.marx_labs.ads.db.model.format.AdFormat;
import de.marx_labs.ads.db.model.format.impl.FullBannerAdFormat;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.ExternAdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdTypes;

public class SearchTest {
	public SearchTest () {
		
	}
	
	public void doDaySearchTest () throws Exception {
		System.out.println("Day");
		AdDBManager manager = AdDBManager.builder().build();
		
		manager.getAdDB().open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		DayConditionDefinition ddef = new DayConditionDefinition();
		ddef.addDay(Day.Saturday);
		b.addConditionDefinition(ConditionDefinitions.DAY, ddef);
		
		
		b.setFormat(new FullBannerAdFormat());
		manager.getAdDB().addBanner(b);
		
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		request.setDay(Day.Monday);
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		System.out.println(result);
		
		request.setDay(Day.Saturday);
		
		result = manager.getAdDB().search(request);
		System.out.println(result);
		
		manager.getAdDB().close();
	}
	
	public void doStateSearchTest () throws Exception {
		System.out.println("State");
		AdDBManager manager = AdDBManager.builder().build();
		
		manager.getAdDB().open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		StateConditionDefinition sdef = new StateConditionDefinition();
		sdef.addState(new State("BB"));
		b.addConditionDefinition(ConditionDefinitions.STATE, sdef);
		
		b.setFormat(new FullBannerAdFormat());
		manager.getAdDB().addBanner(b);
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		request.setState(new State("BE"));
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		System.out.println(result);
		
		request.setState(new State("BB"));
		
		result = manager.getAdDB().search(request);
		System.out.println(result);
		
		manager.getAdDB().close();
	}
	
	public void doTimeSearchTest () throws Exception {
		System.out.println("Time");
		AdDBManager manager = AdDBManager.builder().build();
		
		manager.getAdDB().open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		TimeConditionDefinition tdef = new TimeConditionDefinition();
		
		tdef.addPeriod("1000", "2000");
		
		b.addConditionDefinition(ConditionDefinitions.TIME, tdef);

		b.setFormat(new FullBannerAdFormat());
		manager.getAdDB().addBanner(b);
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		request.setTime("0800");
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		System.out.println(result);
		
		request.setTime("1100");
		result = manager.getAdDB().search(request);
		System.out.println(result);
		
		manager.getAdDB().close();
	}
	
	public void doDateSearchTest () throws Exception {
		System.out.println("Date");
		AdDBManager manager = AdDBManager.builder().build();
		
		manager.getAdDB().open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		DateConditionDefinition dateDef = new DateConditionDefinition();
		dateDef.addPeriod("20100623", "20100723");
		b.addConditionDefinition(ConditionDefinitions.DATE, dateDef);
		
		b.setFormat(new FullBannerAdFormat());
		manager.getAdDB().addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("1");
		
		dateDef = new DateConditionDefinition();
		dateDef.addPeriod("20100623", null);
		b.addConditionDefinition(ConditionDefinitions.DATE, dateDef);
		
		b.setFormat(new FullBannerAdFormat());
		manager.getAdDB().addBanner(b);
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		request.setDate("20100623");
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		System.out.println(result);
		
		request.setDate("20100724");
		result = manager.getAdDB().search(request);
		System.out.println(result);
		
		manager.getAdDB().reopen();
	}
	
	public void doCountrySearchTest () throws Exception {
		System.out.println("Country");
		AdDBManager manager = AdDBManager.builder().build();
		
		manager.getAdDB().open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		CountryConditionDefinition cdef = new CountryConditionDefinition();
		cdef.addCountry(new Country("DE"));
		b.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);
		
		b.setFormat(new FullBannerAdFormat());
		manager.getAdDB().addBanner(b);
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ImageAdType.TYPE));
		request.setTypes(types);
		request.setCountry(new Country("DE"));
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		System.out.println(result);
		
		request.setCountry(new Country("UK"));
		
		result = manager.getAdDB().search(request);
		System.out.println(result);
		
		manager.getAdDB().close();
	}
	
	public void doKeywordSearchTest () throws Exception {
		System.out.println("Keyword");
		AdDBManager manager = AdDBManager.builder().build();
		
		manager.getAdDB().open();
		
		AdDefinition b = new ExternAdDefinition();
		b.setId("1");

		CountryConditionDefinition cdef = new CountryConditionDefinition();
		cdef.addCountry(new Country("DE"));
		b.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);

		b.setFormat(new FullBannerAdFormat());
		Keyword kw = new Keyword("Esprit");
		KeywordConditionDefinition kdef = new KeywordConditionDefinition();
		kdef.addKeyword(kw);
		b.addConditionDefinition(ConditionDefinitions.KEYWORD, cdef);
		manager.getAdDB().addBanner(b);
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ExternAdType.TYPE));
		request.setTypes(types);
		
		request.getKeywords().add("Puma");
//		request.getKeywords().add("Esprit");
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		System.out.println(result.size());
		
		manager.getAdDB().close();
	}
	
	public void doSiteSearchTest () throws Exception {
		System.out.println("Site test");
		AdDBManager manager = AdDBManager.builder().build();
		
		manager.getAdDB().open();
		
		AdDefinition b = new ExternAdDefinition();
		b.setId("1");
		
		CountryConditionDefinition cdef = new CountryConditionDefinition();
		cdef.addCountry(new Country("DE"));
		b.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);
		b.setFormat(new FullBannerAdFormat());

		SiteConditionDefinition sdef = new SiteConditionDefinition();
		sdef.addSite("test_site");
		b.addConditionDefinition(ConditionDefinitions.SITE, sdef);
		
		manager.getAdDB().addBanner(b);
		
		manager.getAdDB().reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(new FullBannerAdFormat());
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdTypes.forType(ExternAdType.TYPE));
		request.setTypes(types);
		
		
		List<AdDefinition> result = manager.getAdDB().search(request);
		System.out.println(result.size());
		
		request.setSite("demo_site");
		result = manager.getAdDB().search(request);
		System.out.println(result.size());
		
		request.setSite("test_site");
		result = manager.getAdDB().search(request);
		System.out.println(result.size());
		
		manager.getAdDB().close();
	}
	
	public static void main (String [] args) throws Exception {
		SearchTest st = new SearchTest();
		
//		st.doDaySearchTest();
//		System.out.println();
//		st.doTimeSearchTest();
//		System.out.println();
//		st.doStateSearchTest();
//		System.out.println();
//		st.doDateSearchTest();
//		System.out.println();
//		st.doCountrySearchTest();
		
//		st.doKeywordSearchTest();
		st.doSiteSearchTest();
	}
}

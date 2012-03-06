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

import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.Keyword;
import net.mad.ads.db.definition.condition.CountryConditionDefinition;
import net.mad.ads.db.definition.condition.DateConditionDefinition;
import net.mad.ads.db.definition.condition.DayConditionDefinition;
import net.mad.ads.db.definition.condition.KeywordConditionDefinition;
import net.mad.ads.db.definition.condition.SiteConditionDefinition;
import net.mad.ads.db.definition.condition.StateConditionDefinition;
import net.mad.ads.db.definition.condition.TimeConditionDefinition;
import net.mad.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.AdFormat;
import net.mad.ads.db.enums.AdType;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.Country;
import net.mad.ads.db.enums.Day;
import net.mad.ads.db.model.State;

public class SearchTest {
	public SearchTest () {
		
	}
	
	public void doDaySearchTest () throws Exception {
		System.out.println("Day");
		AdDB db = new AdDB();
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		DayConditionDefinition ddef = new DayConditionDefinition();
		ddef.addDay(Day.Saturday);
		b.addConditionDefinition(ConditionDefinitions.DAY, ddef);
		
		
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
		request.setDay(Day.Monday);
		
		List<AdDefinition> result = db.search(request);
		System.out.println(result);
		
		request.setDay(Day.Saturday);
		
		result = db.search(request);
		System.out.println(result);
		
		db.close();
	}
	
	public void doStateSearchTest () throws Exception {
		System.out.println("State");
		AdDB db = new AdDB();
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		StateConditionDefinition sdef = new StateConditionDefinition();
		sdef.addState(new State("BB"));
		b.addConditionDefinition(ConditionDefinitions.STATE, sdef);
		
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
		request.setState(new State("BE"));
		
		List<AdDefinition> result = db.search(request);
		System.out.println(result);
		
		request.setState(new State("BB"));
		
		result = db.search(request);
		System.out.println(result);
		
		db.close();
	}
	
	public void doTimeSearchTest () throws Exception {
		System.out.println("Time");
		AdDB db = new AdDB();
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		TimeConditionDefinition tdef = new TimeConditionDefinition();
		
		tdef.addPeriod("1000", "2000");
		
		b.addConditionDefinition(ConditionDefinitions.TIME, tdef);

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
		request.setTime("0800");
		
		List<AdDefinition> result = db.search(request);
		System.out.println(result);
		
		request.setTime("1100");
		result = db.search(request);
		System.out.println(result);
		
		db.close();
	}
	
	public void doDateSearchTest () throws Exception {
		System.out.println("Date");
		AdDB db = new AdDB();
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		DateConditionDefinition dateDef = new DateConditionDefinition();
		dateDef.addPeriod("20100623", "20100723");
		b.addConditionDefinition(ConditionDefinitions.DATE, dateDef);
		
		b.setFormat(AdFormat.FULL_BANNER);
		db.addBanner(b);
		
		b = new ImageAdDefinition();
		b.setId("1");
		
		dateDef = new DateConditionDefinition();
		dateDef.addPeriod("20100623", null);
		b.addConditionDefinition(ConditionDefinitions.DATE, dateDef);
		
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
		request.setDate("20100623");
		
		List<AdDefinition> result = db.search(request);
		System.out.println(result);
		
		request.setDate("20100724");
		result = db.search(request);
		System.out.println(result);
		
		db.reopen();
	}
	
	public void doCountrySearchTest () throws Exception {
		System.out.println("Country");
		AdDB db = new AdDB();
		
		db.open();
		
		AdDefinition b = new ImageAdDefinition();
		b.setId("1");
		
		CountryConditionDefinition cdef = new CountryConditionDefinition();
		cdef.addCountry(new Country("DE"));
		b.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);
		
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
		request.setCountry(new Country("DE"));
		
		List<AdDefinition> result = db.search(request);
		System.out.println(result);
		
		request.setCountry(new Country("UK"));
		
		result = db.search(request);
		System.out.println(result);
		
		db.close();
	}
	
	public void doKeywordSearchTest () throws Exception {
		System.out.println("Keyword");
		AdDB db = new AdDB();
		
		db.open();
		
		AdDefinition b = new ExternAdDefinition();
		b.setId("1");

		CountryConditionDefinition cdef = new CountryConditionDefinition();
		cdef.addCountry(new Country("DE"));
		b.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);

		b.setFormat(AdFormat.FULL_BANNER);
		Keyword kw = new Keyword("Esprit");
		KeywordConditionDefinition kdef = new KeywordConditionDefinition();
		kdef.addKeyword(kw);
		b.addConditionDefinition(ConditionDefinitions.KEYWORD, cdef);
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(AdFormat.FULL_BANNER);
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdType.EXTERN);
		request.setTypes(types);
		
		request.getKeywords().add("Puma");
//		request.getKeywords().add("Esprit");
		
		List<AdDefinition> result = db.search(request);
		System.out.println(result.size());
		
		db.close();
	}
	
	public void doSiteSearchTest () throws Exception {
		System.out.println("Site test");
		AdDB db = new AdDB();
		
		db.open();
		
		AdDefinition b = new ExternAdDefinition();
		b.setId("1");
		
		CountryConditionDefinition cdef = new CountryConditionDefinition();
		cdef.addCountry(new Country("DE"));
		b.addConditionDefinition(ConditionDefinitions.COUNTRY, cdef);
		b.setFormat(AdFormat.FULL_BANNER);

		SiteConditionDefinition sdef = new SiteConditionDefinition();
		sdef.addSite("test_site");
		b.addConditionDefinition(ConditionDefinitions.SITE, sdef);
		
		db.addBanner(b);
		
		db.reopen();
		
		AdRequest request = new AdRequest();
		List<AdFormat> formats = new ArrayList<AdFormat>();
		formats.add(AdFormat.FULL_BANNER);
		request.setFormats(formats);
		List<AdType> types = new ArrayList<AdType>();
		types.add(AdType.EXTERN);
		request.setTypes(types);
		
		
		List<AdDefinition> result = db.search(request);
		System.out.println(result.size());
		
		request.setSite("demo_site");
		result = db.search(request);
		System.out.println(result.size());
		
		request.setSite("test_site");
		result = db.search(request);
		System.out.println(result.size());
		
		db.close();
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

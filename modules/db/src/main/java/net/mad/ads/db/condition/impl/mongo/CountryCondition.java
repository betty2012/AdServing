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
package net.mad.ads.db.condition.impl.mongo;


import java.util.ArrayList;
import java.util.List;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.CountryConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.enums.Day;
import net.mad.ads.db.model.Country;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;

/**
 * Bedingung für das Land in dem ein Banner angezeigt werden soll
 * 
 * @author tmarx
 *
 */
public class CountryCondition implements Condition <BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		if (request.getCountry() == null) {
			return;
		}
		
		String country = request.getCountry().getCode();
		if (country.equals(Country.ALL.getCode())) {
			return;
		}
		
		List<String> countries = new ArrayList<String>();
		countries.add(country.toLowerCase());
		countries.add(Country.ALL.getCode());
		
		BasicDBObject dq = new BasicDBObject(AdDBConstants.ADDB_AD_COUNTRY, new BasicDBObject("$in", countries));
		
		builder.and(dq);
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		
		CountryConditionDefinition cdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.COUNTRY)) {
			cdef = (CountryConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.COUNTRY);
		}
		
		if (cdef != null && cdef.getCountries().size() > 0) {
			List<Country> list = cdef.getCountries();
			List<String> countries = new ArrayList<String>();
			for (Country c : list) {
				countries.add(c.getCode().toLowerCase());
			}
			bannerDoc.put(AdDBConstants.ADDB_AD_COUNTRY, countries);
		} else {
			List<String> countries = new ArrayList<String>();
			countries.add(Country.ALL.getCode());
			bannerDoc.put(AdDBConstants.ADDB_AD_COUNTRY, countries);
		}
	}

}

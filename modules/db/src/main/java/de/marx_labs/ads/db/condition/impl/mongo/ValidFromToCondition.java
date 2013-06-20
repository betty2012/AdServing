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
package de.marx_labs.ads.db.condition.impl.mongo;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.BooleanQuery;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.DateConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ValidFromToConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ValidFromToConditionDefinition.Period;
import de.marx_labs.ads.db.enums.ConditionDefinitions;

/**
 * Bedingung bzgl. des Zeitraums in dem das Banner angezeigt werden soll z.B.
 * zwischen 23 und 4 Uhr
 * 
 * 04.07.2011: Nach der Anpassung können im Banner nun Perioden angegeben
 * werden, wann ein Banner angezeigt werden soll zwischen 8 und 10 Uhr und
 * zwischen 12 und 14 Uhr
 * 
 * @author tmarx
 * 
 */
public class ValidFromToCondition implements Condition<BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		if (request.getDate() != null && request.getTime() != null) {
			
			String currentDate = request.getDate() + request.getTime();
			
			QueryBuilder condition = QueryBuilder.start();
			
			// from part of the query
			BasicDBObject query_from_1 = new BasicDBObject(AdDBConstants.ADDB_AD_VALID_FROM, new BasicDBObject("$lte", currentDate));
			BasicDBObject query_from_2 = new BasicDBObject(AdDBConstants.ADDB_AD_VALID_FROM, AdDBConstants.ADDB_AD_VALID_ALL);
			
			List<BasicDBObject> queries = new ArrayList<BasicDBObject>();
			queries.add(query_from_1);
			queries.add(query_from_2);
			BasicDBObject query_from = new BasicDBObject("$or", queries);
			
			condition.and(query_from);
			
			// to part of the query
			BasicDBObject query_to_1 = new BasicDBObject(AdDBConstants.ADDB_AD_VALID_TO, new BasicDBObject("$gte", currentDate));
			BasicDBObject query_to_2 = new BasicDBObject(AdDBConstants.ADDB_AD_VALID_TO, AdDBConstants.ADDB_AD_VALID_ALL);
			
			queries = new ArrayList<BasicDBObject>();
			queries.add(query_to_1);
			queries.add(query_to_2);
			BasicDBObject query_to = new BasicDBObject("$or", queries);
			
			condition.and(query_to);
			
			builder.and(condition.get());
		}
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		ValidFromToConditionDefinition tdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.VALIDFROMTO)) {
			tdef = (ValidFromToConditionDefinition) bannerDefinition
					.getConditionDefinition(ConditionDefinitions.VALIDFROMTO);
		}

		if (tdef != null && tdef.getPeriod() != null) {
			/*
			 * Um die Paare von/zu der Perioden zu erhalten, werden die
			 * jeweilige Felder geprefixt. Dadurch können bei der Suche die
			 * einzelnen Perioden unterschieden werden
			 */
			Period p = tdef.getPeriod();
			if (p.getFrom() != null) {
				bannerDoc.put(AdDBConstants.ADDB_AD_VALID_FROM, p.getFrom());
			} else {
				bannerDoc.put(AdDBConstants.ADDB_AD_VALID_FROM, AdDBConstants.ADDB_AD_VALID_ALL);
			}

			if (p.getTo() != null) {
				bannerDoc.put(AdDBConstants.ADDB_AD_VALID_TO, p.getTo());
			} else {
				bannerDoc.put(AdDBConstants.ADDB_AD_VALID_TO, AdDBConstants.ADDB_AD_VALID_ALL);
			}
		} else {
			bannerDoc.put(AdDBConstants.ADDB_AD_VALID_FROM, AdDBConstants.ADDB_AD_VALID_ALL);
			bannerDoc.put(AdDBConstants.ADDB_AD_VALID_TO, AdDBConstants.ADDB_AD_VALID_ALL);
		}
	}
}

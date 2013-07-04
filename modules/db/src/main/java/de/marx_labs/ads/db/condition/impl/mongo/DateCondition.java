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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.DateConditionDefinition;
import de.marx_labs.ads.db.definition.condition.DateConditionDefinition.Period;
import de.marx_labs.ads.db.enums.ConditionDefinitions;

/**
 * Datums Bedingung an denen ein Banner angezeigt werden soll.
 * So ist es z.B. möglich ein Banner nur in der Weihnachtszeit anzuzeigten
 * 
 * @author thmarx
 *
 */
public class DateCondition implements Condition<BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		if (request.date() != null) { 
			List<DBObject> condition_queries = new ArrayList<DBObject>();
			
			/*
			 * Prefix der Indizieren beachten.
			 */
			for (int i = 0; i < DateConditionDefinition.MAX_PERIOD_COUNT; i++) {
			
				QueryBuilder condition = QueryBuilder.start();
				
				// from part of the query
				BasicDBObject query_from_1 = new BasicDBObject(AdDBConstants.ADDB_AD_DATE_FROM + i, new BasicDBObject("$lte", request.date()));
				BasicDBObject query_from_2 = new BasicDBObject(AdDBConstants.ADDB_AD_DATE_FROM + i, AdDBConstants.ADDB_AD_DATE_ALL);
				
				List<BasicDBObject> queries = new ArrayList<BasicDBObject>();
				queries.add(query_from_1);
				queries.add(query_from_2);
				BasicDBObject query_from = new BasicDBObject("$or", queries);
				
				condition.and(query_from);
				
				// to part of the query
				BasicDBObject query_to_1 = new BasicDBObject(AdDBConstants.ADDB_AD_DATE_TO + i, new BasicDBObject("$gte", request.date()));
				BasicDBObject query_to_2 = new BasicDBObject(AdDBConstants.ADDB_AD_DATE_TO + i, AdDBConstants.ADDB_AD_DATE_ALL);
				
				queries = new ArrayList<BasicDBObject>();
				queries.add(query_to_1);
				queries.add(query_to_2);
				BasicDBObject query_to = new BasicDBObject("$or", queries);
				
				condition.and(query_to);

				condition_queries.add(condition.get());
			}	
			builder.and(new BasicDBObject("$or", condition_queries));
		}

	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		
		DateConditionDefinition ddef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.DATE)) {
			ddef = (DateConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.DATE);
		}
		
		if (ddef != null && !ddef.getPeriods().isEmpty()) {
			/*
			 * 	Um die Paare von/zu der Perioden zu erhalten, werden die jeweilige Felder geprefixt.
			 *  Dadurch können bei der Suche die einzelnen Perioden unterschieden werden
			 */
			int count = 0;
			for (Period p : ddef.getPeriods()) {
				if (p.getFrom() != null) {
					bannerDoc.put(AdDBConstants.ADDB_AD_DATE_FROM + count, p.getFrom());
				} else {
					bannerDoc.put(AdDBConstants.ADDB_AD_DATE_FROM + count, AdDBConstants.ADDB_AD_DATE_ALL);
				}
				
				if (p.getTo() != null) {
					bannerDoc.put(AdDBConstants.ADDB_AD_DATE_TO + count, p.getTo());
				} else {
					bannerDoc.put(AdDBConstants.ADDB_AD_DATE_TO + count, AdDBConstants.ADDB_AD_DATE_ALL);
				}
				count++;
			}
//			for (; count < DateConditionDefinition.MAX_PERIOD_COUNT; count++) {
//				bannerDoc.put(AdDBConstants.ADDB_AD_DATE_FROM + count, AdDBConstants.ADDB_AD_DATE_ALL);
//				bannerDoc.put(AdDBConstants.ADDB_AD_DATE_TO + count, AdDBConstants.ADDB_AD_DATE_ALL);
//			}
		} else {
			bannerDoc.put(AdDBConstants.ADDB_AD_DATE_FROM + 0, AdDBConstants.ADDB_AD_DATE_ALL);
			bannerDoc.put(AdDBConstants.ADDB_AD_DATE_TO + 0, AdDBConstants.ADDB_AD_DATE_ALL);
		}
	}

}

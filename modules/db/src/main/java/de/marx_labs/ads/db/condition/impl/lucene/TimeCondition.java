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
package de.marx_labs.ads.db.condition.impl.lucene;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.TimeConditionDefinition;
import de.marx_labs.ads.db.definition.condition.TimeConditionDefinition.Period;
import de.marx_labs.ads.db.enums.ConditionDefinitions;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;

/**
 * Bedingung bzgl. des Zeitraums in dem das Banner angezeigt werden soll
 * z.B. zwischen 23 und 4 Uhr 
 * 
 * 04.07.2011:
 * Nach der Anpassung können im Banner nun Perioden angegeben werden, wann ein Banner
 * angezeigt werden soll 
 * zwischen 8 und 10 Uhr und zwischen 12 und 14 Uhr 
 * 
 * @author tmarx
 *
 */
public class TimeCondition implements Condition<Document, BooleanQuery> {

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		BooleanQuery query = null;
		
		if (request.getTime() != null) { 
			
			BooleanQuery tempquery = new BooleanQuery();
			
			/*
			 * Prefix der Indizieren beachten.
			 */
			for (int i = 0; i < TimeConditionDefinition.MAX_PERIOD_COUNT; i++) {
				query = new BooleanQuery();
				
				BooleanQuery temp = new BooleanQuery();
				TermRangeQuery tQuery = new TermRangeQuery(AdDBConstants.ADDB_AD_TIME_FROM + i, new BytesRef("0000"), new BytesRef(request.getTime()), true, true);
				temp.add(tQuery, Occur.SHOULD);
				temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_TIME_FROM + i, AdDBConstants.ADDB_AD_TIME_ALL)), Occur.SHOULD);
				
				query.add(temp, Occur.MUST);
				
				temp = new BooleanQuery();
				tQuery = new TermRangeQuery(AdDBConstants.ADDB_AD_TIME_TO + i, new BytesRef(request.getTime()), new BytesRef("2500"), true, true);
				temp.add(tQuery, Occur.SHOULD);
				temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_TIME_TO + i, AdDBConstants.ADDB_AD_TIME_ALL)), Occur.SHOULD);
				
				query.add(temp, Occur.MUST);
				
				tempquery.add(query, Occur.SHOULD);
			}	
			mainQuery.add(tempquery, Occur.MUST);
		}
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		TimeConditionDefinition tdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.TIME)) {
			tdef = (TimeConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.TIME);
		}
		
		if (tdef != null && !tdef.getPeriods().isEmpty()) {
			/*
			 * 	Um die Paare von/zu der Perioden zu erhalten, werden die jeweilige Felder geprefixt.
			 *  Dadurch können bei der Suche die einzelnen Perioden unterschieden werden
			 */
			int count = 0;
			for (Period p : tdef.getPeriods()) {
				if (p.getFrom() != null) {
					bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_TIME_FROM + count, p.getFrom(), Field.Store.NO));
				} else {
					bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_TIME_FROM + count, AdDBConstants.ADDB_AD_TIME_ALL, Field.Store.NO));
				}
				
				if (p.getFrom() != null) {
					bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_TIME_TO + count, p.getTo(), Field.Store.NO));
				} else {
					bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_TIME_TO + count, AdDBConstants.ADDB_AD_TIME_ALL, Field.Store.NO));
				}
				count++;
			}
		} else {
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_TIME_FROM + 0, AdDBConstants.ADDB_AD_TIME_ALL, Field.Store.NO));
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_TIME_TO + 0, AdDBConstants.ADDB_AD_TIME_ALL, Field.Store.NO));
		}
	}

}

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
package net.mad.ads.db.condition.impl.lucene;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.DateConditionDefinition;
import net.mad.ads.db.definition.condition.DateConditionDefinition.Period;
import net.mad.ads.db.definition.condition.TimeConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;

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
 * Datums Bedingung an denen ein Banner angezeigt werden soll.
 * So ist es z.B. möglich ein Banner nur in der Weihnachtszeit anzuzeigten
 * 
 * @author thmarx
 *
 */
public class DateCondition implements Condition<Document, BooleanQuery> {

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		if (request.getDate() != null) { 
			BooleanQuery tempquery = new BooleanQuery();
			
			/*
			 * Prefix der Indizieren beachten.
			 */
			for (int i = 0; i < TimeConditionDefinition.MAX_PERIOD_COUNT; i++) {
				BooleanQuery query = new BooleanQuery();
				
				BooleanQuery temp = new BooleanQuery();
				TermRangeQuery tQuery = new TermRangeQuery(AdDBConstants.ADDB_AD_DATE_FROM + i, null, new BytesRef(request.getDate()), true, true);
				temp.add(tQuery, Occur.SHOULD);
				temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_DATE_FROM + i, AdDBConstants.ADDB_AD_DATE_ALL)), Occur.SHOULD);
				
				query.add(temp, Occur.MUST);
				
				temp = new BooleanQuery();
				tQuery = new TermRangeQuery(AdDBConstants.ADDB_AD_DATE_TO + i, new BytesRef(request.getDate()), null, true, true);
				temp.add(tQuery, Occur.SHOULD);
				temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_DATE_TO + i, AdDBConstants.ADDB_AD_DATE_ALL)), Occur.SHOULD);
				
				query.add(temp, Occur.MUST);
				
				tempquery.add(query, Occur.SHOULD);
			}	
			mainQuery.add(tempquery, Occur.MUST);
		}

	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		
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
					bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_DATE_FROM + count, p.getFrom(), Field.Store.NO));
				} else {
					bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_DATE_FROM + count, AdDBConstants.ADDB_AD_DATE_ALL, Field.Store.NO));
				}
				
				if (p.getFrom() != null) {
					bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_DATE_TO + count, p.getTo(), Field.Store.NO));
				} else {
					bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_DATE_TO + count, AdDBConstants.ADDB_AD_DATE_ALL, Field.Store.NO));
				}
				count++;
			}
		} else {
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_DATE_FROM + 0, AdDBConstants.ADDB_AD_DATE_ALL, Field.Store.NO));
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_DATE_TO + 0, AdDBConstants.ADDB_AD_DATE_ALL, Field.Store.NO));
		}
	}

}

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
package de.marx_labs.ads.db.condition.impl.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.util.BytesRef;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;

import de.marx_labs.ads.db.definition.condition.ValidFromToConditionDefinition;
import de.marx_labs.ads.db.definition.condition.ValidFromToConditionDefinition.Period;
import de.marx_labs.ads.db.enums.ConditionDefinitions;

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
public class ValidFromToCondition implements Condition<Document, BooleanQuery> {

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		if (request.getTime() != null && request.getDate() != null) { 
			
			// build the correct format
			String currentDate = request.getDate() + request.getTime();
			
			BooleanQuery query = new BooleanQuery();
			
			BooleanQuery temp = new BooleanQuery();
			TermRangeQuery tQuery = new TermRangeQuery(AdDBConstants.ADDB_AD_VALID_FROM, null, new BytesRef(currentDate), true, true);
			temp.add(tQuery, Occur.SHOULD);
			temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_VALID_FROM, AdDBConstants.ADDB_AD_VALID_ALL)), Occur.SHOULD);
			
			query.add(temp, Occur.MUST);
			
			temp = new BooleanQuery();
			tQuery = new TermRangeQuery(AdDBConstants.ADDB_AD_VALID_TO, new BytesRef(currentDate), null, true, true);
			temp.add(tQuery, Occur.SHOULD);
			temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_VALID_TO, AdDBConstants.ADDB_AD_VALID_ALL)), Occur.SHOULD);
			
			query.add(temp, Occur.MUST);
			
			mainQuery.add(query, Occur.MUST);
			
		}
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		ValidFromToConditionDefinition tdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.VALIDFROMTO)) {
			tdef = (ValidFromToConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.VALIDFROMTO);
		}
		
		if (tdef != null && tdef.getPeriod() != null) {
			Period p = tdef.getPeriod();
			if (p.getFrom() != null) {
				bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_VALID_FROM, p.getFrom(), Field.Store.NO));
			} else {
				bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_VALID_FROM, AdDBConstants.ADDB_AD_VALID_ALL, Field.Store.NO));
			}
			
			if (p.getTo() != null) {
				bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_VALID_TO, p.getTo(), Field.Store.NO));
			} else {
				bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_VALID_TO, AdDBConstants.ADDB_AD_VALID_ALL, Field.Store.NO));
			}
		} else {
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_VALID_FROM, AdDBConstants.ADDB_AD_VALID_ALL, Field.Store.NO));
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_VALID_TO, AdDBConstants.ADDB_AD_VALID_ALL, Field.Store.NO));
		}
	}

}

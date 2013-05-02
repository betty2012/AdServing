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

import java.util.List;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.Keyword;
import de.marx_labs.ads.db.definition.condition.KeywordConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;


/**
 * Keyword-Bedingung
 * 
 * Die Klasse erweitert das Document und das Query
 * 
 * @author tmarx
 *
 */
public class KeywordCondition implements Condition<Document, BooleanQuery> {

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		if (request.getKeywords() == null || request.getKeywords().size() == 0) {
			return;
		}
		
		BooleanQuery query = new BooleanQuery();
		
		BooleanQuery temp = new BooleanQuery();
		
		// keywords einfügen
		for (String k : request.getKeywords()) {
			temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_KEYWORD, k)), Occur.SHOULD);
		}
		// all keywords einfügen
		temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_KEYWORD, AdDBConstants.ADDB_AD_KEYWORD_ALL)), Occur.SHOULD);
		
		query.add(temp, Occur.MUST);
		mainQuery.add(query, Occur.MUST);
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		
		KeywordConditionDefinition kdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.KEYWORD)) {
			kdef = (KeywordConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.KEYWORD);
		}
		
		if (kdef != null && kdef.getKeywords().size() > 0) {
			// keywords im Dokument speichern
			List<Keyword> kws = kdef.getKeywords();
			for (Keyword k : kws) {
				bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_KEYWORD, k.word, Field.Store.NO));
			}
		} else {
			/*
			 * für alle Banner ohne angegebenem Keyword wird das default ALL-Keyword gesetzt
			 */
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_KEYWORD, AdDBConstants.ADDB_AD_KEYWORD_ALL, Field.Store.NO));
		}
	}

}

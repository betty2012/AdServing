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
import net.mad.ads.db.definition.Keyword;
import net.mad.ads.db.definition.condition.KeywordConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
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
 * Keyword-Bedingung
 * 
 * Die Klasse erweitert das Document und das Query
 * 
 * @author tmarx
 *
 */
public class KeywordCondition implements Condition<BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		if (request.getKeywords() == null || request.getKeywords().size() == 0) {
			return;
		}
		
		
		List<String> keywords = new ArrayList<String>();
		for (String k : request.getKeywords()) {
			keywords.add(k);
		}
		keywords.add(AdDBConstants.ADDB_AD_KEYWORD_ALL);
		
		BasicDBObject dq = new BasicDBObject(AdDBConstants.ADDB_AD_KEYWORD, new BasicDBObject("$in", keywords));
		
		builder.and(dq);
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		
		KeywordConditionDefinition kdef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.KEYWORD)) {
			kdef = (KeywordConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.KEYWORD);
		}
		
		if (kdef != null && kdef.getKeywords().size() > 0) {
			// keywords im Dokument speichern
			List<Keyword> kws = kdef.getKeywords();
			List<String> keywords = new ArrayList<String>();
			for (Keyword k : kws) {
				keywords.add(k.word);
			}
			bannerDoc.put(AdDBConstants.ADDB_AD_KEYWORD, keywords);
		} else {
			/*
			 * für alle Banner ohne angegebenem Keyword wird das default ALL-Keyword gesetzt
			 */
			List<String> keywords = new ArrayList<String>();
			keywords.add(AdDBConstants.ADDB_AD_KEYWORD_ALL);
			bannerDoc.put(AdDBConstants.ADDB_AD_KEYWORD, keywords);
		}
	}

}

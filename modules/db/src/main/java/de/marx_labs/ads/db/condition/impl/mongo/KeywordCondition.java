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
import com.mongodb.QueryBuilder;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.Keyword;
import de.marx_labs.ads.db.definition.condition.KeywordConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;


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

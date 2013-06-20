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
import de.marx_labs.ads.db.definition.condition.LanguageConditionDefinition;
import de.marx_labs.ads.db.definition.condition.StateConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.State;

public class LanguageCondition implements Condition<BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		if (request.getLocale() == null) {
			return;
		}
		String language = request.getLocale().getLanguage();
		if (language.equals(AdDBConstants.ADDB_AD_LANGUAGE_ALL)) {
			return;
		}
		
		List<String> states = new ArrayList<String>();
		states.add(language);
		states.add(AdDBConstants.ADDB_AD_LANGUAGE_ALL);
		
		BasicDBObject dq = new BasicDBObject(AdDBConstants.ADDB_AD_LANGUAGE, new BasicDBObject("$in", states));
		
		builder.and(dq);
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		
		LanguageConditionDefinition stDef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.LANGUAGE)) {
			stDef = (LanguageConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.LANGUAGE);
		}
		
		if (stDef != null && stDef.getLanguages().size() > 0) {
			List<String> languages = stDef.getLanguages();
			
			bannerDoc.put(AdDBConstants.ADDB_AD_LANGUAGE, languages);
		} else {
			List<String> states = new ArrayList<String>();
			states.add(AdDBConstants.ADDB_AD_LANGUAGE_ALL);
			bannerDoc.put(AdDBConstants.ADDB_AD_LANGUAGE, states);
		}
	}

}

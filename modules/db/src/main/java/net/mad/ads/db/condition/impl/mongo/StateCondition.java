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
import net.mad.ads.db.definition.condition.StateConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.Country;
import net.mad.ads.db.model.State;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;

public class StateCondition implements Condition<BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		if (request.getState() == null) {
			return;
		}
		String state = request.getState().getCode();
		if (state.equals(State.ALL.getCode())) {
			return;
		}
		
		List<String> states = new ArrayList<String>();
		states.add(state.toLowerCase());
		states.add(State.ALL.getCode());
		
		BasicDBObject dq = new BasicDBObject(AdDBConstants.ADDB_AD_STATE, new BasicDBObject("$in", states));
		
		builder.and(dq);
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		
		StateConditionDefinition stDef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.STATE)) {
			stDef = (StateConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.STATE);
		}
		
		if (stDef != null && stDef.getStates().size() > 0) {
			List<State> list = stDef.getStates();
			List<String> states = new ArrayList<String>();
			for (State state : list) {
				states.add(state.getCode().toLowerCase());
			}
			bannerDoc.put(AdDBConstants.ADDB_AD_STATE, states);
		} else {
			List<String> states = new ArrayList<String>();
			states.add(AdDBConstants.ADDB_AD_STATE_ALL);
			bannerDoc.put(AdDBConstants.ADDB_AD_STATE, states);
		}
	}

}

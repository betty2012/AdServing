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

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.StateConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.model.State;

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

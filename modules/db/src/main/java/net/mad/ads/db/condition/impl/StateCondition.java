/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.mad.ads.db.condition.impl;

import java.util.List;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.StateConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.model.State;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

public class StateCondition implements Condition {

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		if (request.getState() == null) {
			return;
		}
		String state = request.getState().getCode();
		if (state.equals(State.ALL.getCode())) {
			return;
		}
		BooleanQuery query = new BooleanQuery();
		
		BooleanQuery temp = new BooleanQuery();
		temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_STATE, state.toLowerCase())), Occur.SHOULD);
		temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_STATE, State.ALL.getCode())), Occur.SHOULD);
		
		query.add(temp, Occur.MUST);
		
		mainQuery.add(query, Occur.MUST);
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		
		StateConditionDefinition stDef = null;
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.STATE)) {
			stDef = (StateConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.STATE);
		}
		
		if (stDef != null && stDef.getStates().size() > 0) {
			List<State> list = stDef.getStates();
			for (State state : list) {
				bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_STATE, state.getCode().toLowerCase(), Field.Store.NO));
			}
		} else {
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_STATE, AdDBConstants.ADDB_AD_STATE_ALL, Field.Store.NO));
		}
	}

}

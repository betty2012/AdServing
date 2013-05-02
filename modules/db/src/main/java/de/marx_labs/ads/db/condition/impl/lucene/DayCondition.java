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

import java.util.List;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.condition.Condition;
import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.DayConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.enums.Day;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

/**
 * Bedingung die angiebt, an welchen Tage der Woche ein Banner angezeigt werden soll
 * 
 * 0 = an allen Tagen
 * 1 = Montag
 * 2 = Dienstag
 * 3 = Mittwoch
 * 4 = Donnerstag
 * 5 = Freitag
 * 6 = Samstag
 * 7 = Sonntag
 * 
 * @author thmarx
 *
 */
public class DayCondition implements Condition<Document, BooleanQuery> {

	@Override
	public void addQuery(AdRequest request, BooleanQuery mainQuery) {
		if (request.getDay() == null) {
			return;
		}
		int day = request.getDay().getDay();
		if (day == Day.ALL.getDay()) {
			return;
		}
		BooleanQuery query = new BooleanQuery();
		
		BooleanQuery temp = new BooleanQuery();
		temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_DAY, String.valueOf(day))), Occur.SHOULD);
		temp.add(new TermQuery(new Term(AdDBConstants.ADDB_AD_DAY, String.valueOf(Day.ALL.getDay()))), Occur.SHOULD);
		
		query.add(temp, Occur.MUST);

		mainQuery.add(query, Occur.MUST);
	}

	@Override
	public void addFields(Document bannerDoc, AdDefinition bannerDefinition) {
		DayConditionDefinition ddef = null;
		
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.DAY)) {
			ddef = (DayConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.DAY);
		}
		
		
		if (ddef != null && ddef.getDays().size() > 0) {
			List<Day> list = ddef.getDays();
			for (Day day : list) {
				bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_DAY, String.valueOf(day.getDay()), Field.Store.NO));
			}
		} else {
			bannerDoc.add(new StringField(AdDBConstants.ADDB_AD_DAY, AdDBConstants.ADDB_AD_DAY_ALL, Field.Store.NO));
		}
	}

}

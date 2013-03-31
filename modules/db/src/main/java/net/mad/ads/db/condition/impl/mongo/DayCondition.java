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
import net.mad.ads.db.definition.condition.DayConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.enums.Day;
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
public class DayCondition implements Condition<BasicDBObject, QueryBuilder> {

	@Override
	public void addQuery(AdRequest request, QueryBuilder builder) {
		if (request.getDay() == null) {
			return;
		}
		int day = request.getDay().getDay();
		if (day == Day.ALL.getDay()) {
			return;
		}
		
		List<String> days = new ArrayList<String>();
		days.add(String.valueOf(day));
		days.add(String.valueOf(Day.ALL.getDay()));
		
		BasicDBObject dq = new BasicDBObject(AdDBConstants.ADDB_AD_DAY, new BasicDBObject("$in", days));
		
		builder.and(dq);
	}

	@Override
	public void addFields(BasicDBObject bannerDoc, AdDefinition bannerDefinition) {
		DayConditionDefinition ddef = null;
		
		if (bannerDefinition.hasConditionDefinition(ConditionDefinitions.DAY)) {
			ddef = (DayConditionDefinition) bannerDefinition.getConditionDefinition(ConditionDefinitions.DAY);
		}
		
		
		if (ddef != null && ddef.getDays().size() > 0) {
			List<Day> list = ddef.getDays();
			List<String> days = new ArrayList<String>();
			for (Day day : list) {
				days.add(String.valueOf(day.getDay()));
			}
			bannerDoc.put(AdDBConstants.ADDB_AD_DAY, days);
		} else {
			List<String> days = new ArrayList<String>();
			days.add(AdDBConstants.ADDB_AD_DAY_ALL);
			bannerDoc.put(AdDBConstants.ADDB_AD_DAY, days);
		}
	}

}

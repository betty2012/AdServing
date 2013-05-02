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
package de.marx_labs.ads.db.definition.condition;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import de.marx_labs.ads.db.AdDBConstants;
import de.marx_labs.ads.db.definition.ConditionDefinition;

/**
 * Steuerung zu welchen Daten das Banner angezeigt werden soll
 * 
 * Dadurch ist es möglich, ein Banner nur im Sommer anzuzeigen
 * 01.06.2011 - 31.08.2011 
 * 
 * Durch die Einführung der Perioden, kann geregelt werden, dass ein Banner zu
 * verschiedenen Zeitspannen angezeigt wird:
 * 01.06.2011 - 31.08.2011
 * 01.10.2011 - 31.10.2011
 * 
 * @author tmarx
 *
 */
public class DateConditionDefinition implements ConditionDefinition {

	public static final Period ALL_TIMES = new Period();
	static {
		ALL_TIMES.setFrom(AdDBConstants.ADDB_AD_DATE_ALL);
		ALL_TIMES.setTo(AdDBConstants.ADDB_AD_DATE_ALL);
	}
	public static final int MAX_PERIOD_COUNT = 6;

	private Set<DateConditionDefinition.Period> periods = new HashSet<DateConditionDefinition.Period>();
	
	public DateConditionDefinition () {
		
	}
	
	public Set<DateConditionDefinition.Period> getPeriods() {
		return periods;
	}
	
	public void addPeriod (String from, String to) {
		Period p = new Period();
		if (from != null) {
			p.setFrom(from);
		} else {
			p.setFrom(AdDBConstants.ADDB_AD_DATE_ALL);
		}
		
		if (to != null) {
			p.setTo(to);
		} else {
			p.setTo(AdDBConstants.ADDB_AD_DATE_ALL);
		}
		
		periods.add(p);
	}
	
	public static class Period implements Serializable {
		private String from;
		private String to;
		
		/**
		 * von welchem Datum an soll das banner angezeigt werdne
		 * z.b. 01.02.2010
		 * @return
		 */
		public String getFrom() {
			return this.from;
		}
		public void setFrom(String date) {
			this.from = date;
		}
		/**
		 * bis zu welchem Datum soll das Banner angezegit werden
		 * @return
		 */
		public String getTo() {
			return this.to;
		}
		public void setTo(String date) {
			this.to = date;
		}
	}
}

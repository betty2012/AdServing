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
package net.mad.ads.db.definition.condition;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.definition.ConditionDefinition;

/**
 * Steuerung der Tageszeit, zu der ein Banner angezeigt werden soll
 * So kann gesteuert werden, dass ein Banner nur zwischen 8 und 12 Uhr angezeigt werden soll
 * 
 * @author tmarx
 *
 */
public class TimeConditionDefinition implements ConditionDefinition {
	
	public static final Period ALL_TIMES = new Period();
	static {
		ALL_TIMES.setFrom(AdDBConstants.ADDB_AD_TIME_ALL);
		ALL_TIMES.setTo(AdDBConstants.ADDB_AD_TIME_ALL);
	}
	public static final int MAX_PERIOD_COUNT = 4;

	private Set<TimeConditionDefinition.Period> periods = new HashSet<TimeConditionDefinition.Period>();
	
	public TimeConditionDefinition () {
	}
	
	public Set<TimeConditionDefinition.Period> getPeriods() {
		return periods;
	}
	
	public void addPeriod (String from, String to) {
		Period p = new Period();
		if (from != null) {
			p.setFrom(from);
		} else {
			p.setFrom(AdDBConstants.ADDB_AD_TIME_ALL);
		}
		
		if (to != null) {
			p.setTo(to);
		} else {
			p.setTo(AdDBConstants.ADDB_AD_TIME_ALL);
		}
		
		periods.add(p);
	}

	public static class Period implements Serializable {
		private String from;
		private String to;
		
		public final String getFrom() {
			return from;
		}

		public final void setFrom(String from) {
			this.from = from;
		}

		public final String getTo() {
			return to;
		}

		public final void setTo(String to) {
			this.to = to;
		}
	}
	
	
}

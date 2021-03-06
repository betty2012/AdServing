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

import java.util.ArrayList;
import java.util.List;

import de.marx_labs.ads.db.definition.ConditionDefinition;
import de.marx_labs.ads.db.enums.Day;

/**
 * Steuerung an welchen Wochentagen (Montag - Sonntag) das Banner angezeigt werden soll
 * 
 * @author tmarx
 *
 */
public class DayConditionDefinition implements ConditionDefinition {
	
	private List<Day> days = new ArrayList<Day>();
	
	public DayConditionDefinition () {
		
	}
	
	public List<Day> getDays() {
		return this.days;
	}
	public void addDay(Day day) {
		this.days.add(day);
	}
}

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
package de.marx_labs.ads.db.enums;

import java.io.Serializable;
import java.util.Calendar;

public enum Day implements Serializable {
	
	Monday(1),
	Tuesday(2),
	Wednesday(3),
	Thursday(4),
	Friday(5),
	Saturday(6),
	Sunday(7),
	ALL(0);
	
	private int day = 0;
	private Day (int day) {
		this.day = day;
	}
	
	public int getDay() {
		return day;
	}
	public static Day getDayForInt (int day) throws IllegalArgumentException {
		for (Day d : Day.values()) {
			if (d.getDay() == day) {
				return d;
			}
		}
		throw new IllegalArgumentException("Parameter day=" + day + " not valid");
	}
	
	public static Day getDayForJava (int jDay) {
		
		if (jDay == Calendar.MONDAY) {
			return Day.Monday;
		} else if (jDay == Calendar.TUESDAY) {
			return Day.Tuesday;
		} else if (jDay == Calendar.WEDNESDAY) {
			return Day.Wednesday;
		} else if (jDay == Calendar.THURSDAY) {
			return Day.Thursday;
		} else if (jDay == Calendar.FRIDAY) {
			return Day.Friday;
		} else if (jDay == Calendar.SATURDAY) {
			return Day.Saturday;
		} else if (jDay == Calendar.SUNDAY) {
			return Day.Sunday;
		}
		
		 return ALL;
	}
}

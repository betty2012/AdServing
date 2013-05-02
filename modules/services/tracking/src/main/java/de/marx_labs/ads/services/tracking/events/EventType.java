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
package de.marx_labs.ads.services.tracking.events;

public enum EventType {
	IMPRESSION ("impression"), 
	CLICK ("click"),
	ALL ("all");
	
	private String name;
	
	private EventType (String name) {
		this.name = name;
	}
	
	public String getName () {
		return this.name;
	}
	
	public static EventType forName (String name) {
		if (name == null || name.equals("")) {
			return EventType.ALL;
		}
		
		for (EventType type : values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}
		
		return EventType.ALL;
	}
}

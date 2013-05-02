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
package de.marx_labs.ads.db.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.marx_labs.ads.db.model.type.AdType;


public class AdTypes {
	
	private static List<AdType> types = new ArrayList<AdType>();
	private static Map<String, AdType> nameLookup = new HashMap<String, AdType>();
	private static Map<String, AdType> typeLookup = new HashMap<String, AdType>();
	
	static {
		Collection<AdType> colTypes = (Collection<AdType>) Lookup.lookupAll(AdType.class);
		types.addAll(colTypes);
		
		for (AdType adt : colTypes) {
			nameLookup.put(adt.getName(), adt);
			typeLookup.put(adt.getType(), adt);
		}
	}
	
	public static AdType forType (String type) {
		return typeLookup.get(type);
	}
	
	public static AdType forName (String name) {
		return nameLookup.get(name);
	}
	
	public static List<AdType> getTypes () {
		return types;
	}
	
	
}

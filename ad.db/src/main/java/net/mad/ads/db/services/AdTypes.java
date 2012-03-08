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
package net.mad.ads.db.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mad.ads.db.model.type.AdType;


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

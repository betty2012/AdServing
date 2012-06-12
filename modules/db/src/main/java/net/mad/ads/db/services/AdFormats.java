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

import net.mad.ads.db.model.format.AdFormat;

public class AdFormats {
	
	private static List<AdFormat> formats = new ArrayList<AdFormat>();
	private static Map<String, AdFormat> nameLookup = new HashMap<String, AdFormat>();
	private static Map<String, AdFormat> compoundNameLookup = new HashMap<String, AdFormat>();
	static {
		Collection<AdFormat> colformats = (Collection<AdFormat>) Lookup.lookupAll(AdFormat.class);
		formats.addAll(colformats);
		
		for (AdFormat adf : colformats) {
			nameLookup.put(adf.getName(), adf);
			compoundNameLookup.put(adf.getCompoundName(), adf);
		}
	}
	
	public static AdFormat forCompoundName (String compound) {
		return compoundNameLookup.get(compound);
	}
	
	public static AdFormat forName (String name) {
		return nameLookup.get(name);
	}
	
	public static List<AdFormat> getFormats () {
		return formats;
	}
}

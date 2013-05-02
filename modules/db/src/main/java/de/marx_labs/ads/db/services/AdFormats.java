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

import de.marx_labs.ads.db.model.format.AdFormat;

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

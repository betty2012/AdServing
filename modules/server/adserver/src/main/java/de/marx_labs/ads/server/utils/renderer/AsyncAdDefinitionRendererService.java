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
package de.marx_labs.ads.server.utils.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.services.Lookup;

public class AsyncAdDefinitionRendererService {

	private static List<AsyncAdDefinitionRenderer<AdDefinition>> renderer = new ArrayList<AsyncAdDefinitionRenderer<AdDefinition>>();
	private static Map<AdType, AsyncAdDefinitionRenderer<AdDefinition>> typeLookup = new HashMap<AdType, AsyncAdDefinitionRenderer<AdDefinition>>();
	
	static {
		Collection<AsyncAdDefinitionRenderer<AdDefinition>> colRenderes = (Collection<AsyncAdDefinitionRenderer<AdDefinition>>) Lookup.lookupAll(AsyncAdDefinitionRenderer.class);
		renderer.addAll(colRenderes);
		
		for (AsyncAdDefinitionRenderer<AdDefinition> adt : colRenderes) {
			typeLookup.put(adt.getType(), adt);
		}
	}
	
	public static AsyncAdDefinitionRenderer<AdDefinition> forType (AdType type) {
		System.out.println(typeLookup.size());
		return typeLookup.get(type);
	}
	
	public static List<AsyncAdDefinitionRenderer<AdDefinition>> getRenderer () {
		return renderer;
	}
}

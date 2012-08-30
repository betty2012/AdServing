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
package net.mad.ads.server.utils.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.services.Lookup;

public class AdDefinitionRendererService {

	private static List<AdDefinitionRenderer<AdDefinition>> renderer = new ArrayList<AdDefinitionRenderer<AdDefinition>>();
	private static Map<AdType, AdDefinitionRenderer<AdDefinition>> typeLookup = new HashMap<AdType, AdDefinitionRenderer<AdDefinition>>();
	
	static {
		Collection<AdDefinitionRenderer<AdDefinition>> colRenderes = (Collection<AdDefinitionRenderer<AdDefinition>>) Lookup.lookupAll(AdDefinitionRenderer.class);
		renderer.addAll(colRenderes);
		
		for (AdDefinitionRenderer<AdDefinition> adt : colRenderes) {
			typeLookup.put(adt.getType(), adt);
		}
	}
	
	public static AdDefinitionRenderer<AdDefinition> forType (AdType type) {
		System.out.println(typeLookup.size());
		return typeLookup.get(type);
	}
	
	public static List<AdDefinitionRenderer<AdDefinition>> getRenderer () {
		return renderer;
	}
}

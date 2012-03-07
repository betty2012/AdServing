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
		return typeLookup.get(type);
	}
}

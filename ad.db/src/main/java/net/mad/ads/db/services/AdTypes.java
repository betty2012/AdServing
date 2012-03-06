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
	private static Map<Integer, AdType> idLookup = new HashMap<Integer, AdType>();
	static {
		Collection<AdType> colTypes = (Collection<AdType>) Lookup.lookupAll(AdType.class);
		types.addAll(colTypes);
		
		for (AdType adt : colTypes) {
			nameLookup.put(adt.getName(), adt);
			idLookup.put(adt.getType(), adt);
		}
	}
	
	public static AdType forType (int type) {
		return idLookup.get(type);
	}
}

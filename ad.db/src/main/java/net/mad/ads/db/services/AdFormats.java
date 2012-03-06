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
	
	public static AdFormat fromCompoundName (String compound) {
		return compoundNameLookup.get(compound);
	}
	
	public static AdFormat forName (String name) {
		return nameLookup.get(name);
	}
}

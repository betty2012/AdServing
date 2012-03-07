package net.mad.ads.db.model.type;

import java.io.Serializable;

import net.mad.ads.db.definition.AdDefinition;

public interface AdType extends Serializable, Comparable<AdType> {
	/*
	 * unique String value of the type
	 */
	public String getType();
	
	public String getName ();
	
	public AdDefinition getAdDefinition();
}

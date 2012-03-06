package net.mad.ads.db.model.type;

import java.io.Serializable;

import net.mad.ads.db.definition.AdDefinition;

public interface AdType extends Serializable {
	/*
	 * The int-value of this type
	 * 
	 * TODO: we should not use int here, because every custom adtype has to check that the used id is unique. string should be easier to handle
	 */
	public int getType();
	public String getTypeAsString();
	public String getName ();
	
	public AdDefinition getAdDefinition();
}

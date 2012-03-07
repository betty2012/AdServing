package net.mad.ads.db.model.type.impl;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import net.mad.ads.db.model.type.AbstractAdType;

public class FlashAdType extends AbstractAdType {

	public static final String TYPE = "flash";
	
	public FlashAdType() {
		super("Flash", TYPE);
	}

	@Override
	public AdDefinition getAdDefinition() {
		return new FlashAdDefinition();
	}

}

package net.mad.ads.db.model.type.impl;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import net.mad.ads.db.model.type.AbstractAdType;

public class ExternAdType extends AbstractAdType {

	public ExternAdType(String name, int type) {
		super("Extern", 1);
	}

	@Override
	public AdDefinition getAdDefinition() {
		return new ExternAdDefinition();
	}

}

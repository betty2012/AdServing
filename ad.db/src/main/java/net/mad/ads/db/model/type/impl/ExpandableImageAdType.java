package net.mad.ads.db.model.type.impl;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.expandable.ExpandableImageAdDefinition;
import net.mad.ads.db.model.type.AbstractAdType;

public class ExpandableImageAdType extends AbstractAdType {

	public ExpandableImageAdType(String name, int type) {
		super("ExpandableImage", 5);
	}

	@Override
	public AdDefinition getAdDefinition() {
		return new ExpandableImageAdDefinition();
	}

}

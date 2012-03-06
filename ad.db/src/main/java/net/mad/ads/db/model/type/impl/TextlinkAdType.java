package net.mad.ads.db.model.type.impl;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.text.TextlinkAdDefinition;
import net.mad.ads.db.model.type.AbstractAdType;

public class TextlinkAdType extends AbstractAdType {

	public TextlinkAdType(String name, int type) {
		super("Textlink", 3);
	}

	@Override
	public AdDefinition getAdDefinition() {
		return new TextlinkAdDefinition();
	}

}

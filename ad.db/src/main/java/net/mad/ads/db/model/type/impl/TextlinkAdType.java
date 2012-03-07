package net.mad.ads.db.model.type.impl;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.text.TextlinkAdDefinition;
import net.mad.ads.db.model.type.AbstractAdType;

public class TextlinkAdType extends AbstractAdType {

	public static final String TYPE = "textlink";
	
	public TextlinkAdType() {
		super("Textlink", TYPE);
	}

	@Override
	public AdDefinition getAdDefinition() {
		return new TextlinkAdDefinition();
	}

}

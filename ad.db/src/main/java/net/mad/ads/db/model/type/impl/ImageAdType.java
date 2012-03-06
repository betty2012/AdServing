package net.mad.ads.db.model.type.impl;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.model.type.AbstractAdType;

public class ImageAdType extends AbstractAdType {

	public ImageAdType(String name, int type) {
		super("Image", 2);
	}

	@Override
	public AdDefinition getAdDefinition() {
		return new ImageAdDefinition();
	}

}

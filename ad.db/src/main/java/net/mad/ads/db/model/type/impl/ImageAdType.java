package net.mad.ads.db.model.type.impl;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.model.type.AbstractAdType;

public class ImageAdType extends AbstractAdType {

	public static final String TYPE = "image";
	
	public ImageAdType() {
		super("Image", TYPE);
	}

	@Override
	public AdDefinition getAdDefinition() {
		return new ImageAdDefinition();
	}

}

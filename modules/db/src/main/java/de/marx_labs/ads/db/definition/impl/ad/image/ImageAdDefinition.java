/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package de.marx_labs.ads.db.definition.impl.ad.image;

import de.marx_labs.ads.db.definition.impl.ad.AbstractAdDefinition;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdTypes;

/**
 * Einfaches Imagebanner
 * 
 * @author tmarx
 *
 */
public class ImageAdDefinition extends AbstractAdDefinition {
	
	private String imageUrl = null;
	
	/**
	 * Konstruktor für die vom ImageBanner abgeleiteten BannerTypen wie zum Beispiel das ExpandableImageBanner
	 * 
	 * @param type Der BannerType
	 */
	protected ImageAdDefinition (AdType type) {
		super(type);
	}
	public ImageAdDefinition () {
		super(AdTypes.forType(ImageAdType.TYPE));
	}
	
	public final String getImageUrl() {
		return imageUrl;
	}

	public final void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}

/**
 * Mad-Advertisement
 * Copyright (C) 2011-2013 Thorsten Marx <thmarx@gmx.net>
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
package de.marx_labs.ads.db.definition.impl.ad.expandable;

import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.model.type.impl.ExpandableImageAdType;
import de.marx_labs.ads.db.services.AdTypes;

/**
 * Expandierendes Banner
 * 
 * Das Banner besteht aus zwei Bildern, einem kleinen und einem Großen.
 * Bei Mouseover dem kleinen wird das große Bild rechts oder links daneben angezeigt.
 * 
 * @author thmarx
 *
 */
public class ExpandableImageAdDefinition extends ImageAdDefinition {
	
	private String expandedImageUrl = null;
	
	private String expandedImageWidth = null;
	private String expandedImageHeight = null;
	
	public ExpandableImageAdDefinition () {
		super(AdTypes.forType(ExpandableImageAdType.TYPE));
	}

	public String getExpandedImageUrl() {
		return expandedImageUrl;
	}

	public void setExpandedImageUrl(String expandedImageUrl) {
		this.expandedImageUrl = expandedImageUrl;
	}

	public String getExpandedImageWidth() {
		return expandedImageWidth;
	}

	public void setExpandedImageWidth(String expandedImageWidth) {
		this.expandedImageWidth = expandedImageWidth;
	}

	public String getExpandedImageHeight() {
		return expandedImageHeight;
	}

	public void setExpandedImageHeight(String expandedImageHeight) {
		this.expandedImageHeight = expandedImageHeight;
	}


	
	
}

/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.mad.ads.db.definition.impl.ad.image;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.AbstractAdDefinition;
import net.mad.ads.db.enums.AdFormat;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.services.AdTypes;

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
		super(AdTypes.forType(2));
	}
	
	public final String getImageUrl() {
		return imageUrl;
	}

	public final void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}

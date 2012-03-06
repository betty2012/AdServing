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
package net.mad.ads.db.utils.mapper;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.expandable.ExpandableImageAdDefinition;
import net.mad.ads.db.definition.impl.ad.extern.ExternAdDefinition;
import net.mad.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.definition.impl.ad.text.TextlinkAdDefinition;
import net.mad.ads.db.enums.AdType;

public class AdTypeMapping {
	private static AdTypeMapping INSTANCE = new AdTypeMapping();
	private AdTypeMapping () {
	}
	
	public static AdTypeMapping getInstance () {
		return INSTANCE;
	}
	
	public String getBannerTypeAsString (AdType type) {
		return type.toString();
	}
	
	public AdType getBannerTypeFromString (String type) {
		return AdType.valueOf(type);
	}
	
	/**
	 * Mapping von BannerType auf BannerDefinition
	 * @param type
	 * @return
	 */
	public AdDefinition getDefinition (String type) {
		AdType btype = AdType.valueOf(type);
		AdDefinition def = null;
		
		switch (btype) {
			case IMAGE:
				def = new ImageAdDefinition();
				break;
			case EXTERN:
				def = new ExternAdDefinition();
				break;
			case TEXTLINK:
				def = new TextlinkAdDefinition();
				break;
			case FLASH:
				def = new FlashAdDefinition();
				break;
			case EXPANDABLEIMAGE:
				def = new ExpandableImageAdDefinition();
				break;
		}
		
		return def;
	}
}

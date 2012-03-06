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
package net.mad.ads.db.definition.impl.ad.flash;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.impl.ad.AbstractAdDefinition;
import net.mad.ads.db.enums.AdFormat;
import net.mad.ads.db.services.AdTypes;

/**
 * Einfaches Flashbanner
 * 
 * @author tmarx
 *
 */
public class FlashAdDefinition extends AbstractAdDefinition {
	
	private String movieUrl = null;
	private int minFlashVersion = -1;
	private String fallbackImageUrl = null;
	
	public FlashAdDefinition () {
		super(AdTypes.forType(4));
	}
	
	public final String getMovieUrl() {
		return movieUrl;
	}

	public final void setMovieUrl(String movieUrl) {
		this.movieUrl = movieUrl;
	}

	public int getMinFlashVersion() {
		return minFlashVersion;
	}

	public void setMinFlashVersion(int minFlashVersion) {
		this.minFlashVersion = minFlashVersion;
	}

	public String getFallbackImageUrl() {
		return fallbackImageUrl;
	}

	public void setFallbackImageUrl(String fallbackImageUrl) {
		this.fallbackImageUrl = fallbackImageUrl;
	}
	
	
	
}

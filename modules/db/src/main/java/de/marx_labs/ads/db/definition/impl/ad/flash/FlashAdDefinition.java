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
package de.marx_labs.ads.db.definition.impl.ad.flash;

import de.marx_labs.ads.db.definition.impl.ad.AbstractAdDefinition;
import de.marx_labs.ads.db.model.type.impl.FlashAdType;
import de.marx_labs.ads.db.services.AdTypes;

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
		super(AdTypes.forType(FlashAdType.TYPE));
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

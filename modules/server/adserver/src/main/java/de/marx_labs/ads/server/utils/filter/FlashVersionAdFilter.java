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
package de.marx_labs.ads.server.utils.filter;

import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.flash.FlashAdDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

/**
 * Dieser Filter fitlert FlashBanner aus dem Ergebnis, die eine höhere Flashversion benötigen
 * als sie im Request übegeben wurde.
 * 
 * @author thmarx
 *
 */
public class FlashVersionAdFilter implements Predicate<AdDefinition> {

	private static final Logger logger = LoggerFactory.getLogger(FlashVersionAdFilter.class);
	
	private int version = -1;
	
	public FlashVersionAdFilter (int version) {
		this.version = version;
	}
	
	@Override
	public boolean apply(AdDefinition banner) {
		if (banner instanceof FlashAdDefinition) {
			int minFlash = ((FlashAdDefinition)banner).getMinFlashVersion();
			
			if (minFlash > version) {
				return false;
			}
			return true;
		}
		return false;
//		logger.debug("Requestid: " + RuntimeContext.getRequestBanners().containsKey("pv" + requestID + "_" + banner.getId()));
//		if (RuntimeContext.getRequestBanners().containsKey("pv" + requestID + "_" + banner.getId())) {
//			return false;
//		}
//		
//		return true;
	}

}

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
package de.marx_labs.ads.server.utils.filter;

import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.impl.ad.flash.FlashAdDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

/**
 * Dieser Filter filtert FlashBanner aus dem Ergebnis, die kein Fallback Image haben
 * 
 * @author thmarx
 *
 */
public class FlashImageFallbackAdFilter implements Predicate<AdDefinition> {

	private static final Logger logger = LoggerFactory.getLogger(FlashImageFallbackAdFilter.class);
	
	public FlashImageFallbackAdFilter () {
	}
	
	@Override
	public boolean apply(AdDefinition banner) {
		if (banner instanceof FlashAdDefinition) {
			return ((FlashAdDefinition)banner).getFallbackImageUrl() != null;
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

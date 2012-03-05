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
package net.mad.ads.server.utils.filter;

import net.mad.ads.db.definition.BannerDefinition;
import net.mad.ads.db.definition.impl.banner.flash.FlashBannerDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

/**
 * Dieser Filter filtert FlashBanner aus dem Ergebnis, die kein Fallback Image haben
 * 
 * @author thmarx
 *
 */
public class FlashImageFallbackBannerFilter implements Predicate<BannerDefinition> {

	private static final Logger logger = LoggerFactory.getLogger(FlashImageFallbackBannerFilter.class);
	
	public FlashImageFallbackBannerFilter () {
	}
	
	@Override
	public boolean apply(BannerDefinition banner) {
		if (banner instanceof FlashBannerDefinition) {
			return ((FlashBannerDefinition)banner).getFallbackImageUrl() != null;
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

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
package net.mad.ads.server.utils.filter;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.server.utils.RuntimeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

/**
 * Dieser Filter verhindert das doppelte Anzeigen eines Banners auf der selben 
 * Seite für den selben Benutzer
 * 
 * die request_id ist von allen AdRequest die durch einen Pageview erzeugt werden identisch
 * 
 * @author thmarx
 *
 */
public class DuplicatAdFilter implements Predicate<AdDefinition> {

	private static final Logger logger = LoggerFactory.getLogger(DuplicatAdFilter.class);
	
	private String requestID = null;
	
	public DuplicatAdFilter (String request_id) {
		this.requestID = request_id;
	}
	
	@Override
	public boolean apply(AdDefinition banner) {
		logger.debug("Requestid: " + RuntimeContext.getRequestBanners().containsKey("pv" + requestID + "_" + banner.getId()));
		if (RuntimeContext.getRequestBanners().containsKey("pv" + requestID + "_" + banner.getId())) {
			return false;
		}
		
		return true;
	}

}

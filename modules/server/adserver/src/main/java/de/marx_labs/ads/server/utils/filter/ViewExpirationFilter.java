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

import java.util.Calendar;
import java.util.Locale;

import de.marx_labs.ads.base.utils.exception.ServiceException;
import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.db.definition.condition.ViewExpirationConditionDefinition;
import de.marx_labs.ads.db.enums.ConditionDefinitions;
import de.marx_labs.ads.db.enums.ExpirationResolution;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.services.tracking.Criterion;
import de.marx_labs.ads.services.tracking.events.EventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

/**
 * Filter zum entfernen von Bannern, die die konfigurierte Anzeigehäufigkeit
 * für einen bestimmten Zeitraum schon erreicht oder überschritten haben  
 * 
 * @author tmarx
 *
 */
public class ViewExpirationFilter implements Predicate<AdDefinition> {

	private static final Logger logger = LoggerFactory.getLogger(ViewExpirationFilter.class);
	
//	private static final ViewExpirationFilter FILTER = new ViewExpirationFilter();
//	
//	public static final Predicate<BannerDefinition> getFilter () {
//		return FILTER;
//	}
	
	@Override
	public boolean apply(AdDefinition banner) {
		// Banner enth�lt keine View-Beschr�nkung
		if (!banner.hasConditionDefinition(ConditionDefinitions.VIEW_EXPIRATION)){
			return true;
		} else if (RuntimeContext.getTrackService() == null) {
			return true;
		}
		
		
		/*
		 * 1. Prüfen, ob die Impressions f�r diesen Monat schon erreicht wurden
		 * 2. Prüfen, ob die Impressions f�r diese Woche schon erreicht wurden
		 * 3. Prüfen, ob die Impressions f�r diesen Tag schon erreicht wurden
		 */
		
		ViewExpirationConditionDefinition def = (ViewExpirationConditionDefinition) banner.getConditionDefinition(ConditionDefinitions.VIEW_EXPIRATION);
		
		try {
			if (def.getViewExpirations().containsKey(ExpirationResolution.MONTH)) {
				Calendar from = Calendar.getInstance(Locale.GERMANY);
				from.set(Calendar.HOUR_OF_DAY, 0);
				from.set(Calendar.MINUTE, 0);
				from.set(Calendar.SECOND, 0);
				from.set(Calendar.MILLISECOND, 0);
				
				Calendar to = Calendar.getInstance(Locale.GERMANY);
				to.set(Calendar.HOUR_OF_DAY, 23);
				to.set(Calendar.MINUTE, 59);
				to.set(Calendar.SECOND, 59);
				to.set(Calendar.MILLISECOND, 999);
				
				
				int maxviews = def.getViewExpirations().get(ExpirationResolution.MONTH);
				long impressions = RuntimeContext.getTrackService().count(new Criterion(Criterion.Criteria.Banner, banner.getId()), EventType.IMPRESSION, from.getTime(), to.getTime());
				
				if (impressions <= maxviews) {
					return true;
				}
			}
			if (def.getViewExpirations().containsKey(ExpirationResolution.WEEK)) {
				Calendar from = Calendar.getInstance(Locale.GERMANY);
				from.set(Calendar.HOUR_OF_DAY, 0);
				from.set(Calendar.MINUTE, 0);
				from.set(Calendar.SECOND, 0);
				from.set(Calendar.MILLISECOND, 0);
				from.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				
				Calendar to = Calendar.getInstance(Locale.GERMANY);
				to.set(Calendar.HOUR_OF_DAY, 23);
				to.set(Calendar.MINUTE, 59);
				to.set(Calendar.SECOND, 59);
				to.set(Calendar.MILLISECOND, 999);
				to.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
				
				int maxviews = def.getViewExpirations().get(ExpirationResolution.MONTH);
				long impressions = RuntimeContext.getTrackService().count(new Criterion(Criterion.Criteria.Banner, banner.getId()), EventType.IMPRESSION, from.getTime(), to.getTime());
				
				if (impressions <= maxviews) {
					return true;
				}
			}
			if (def.getViewExpirations().containsKey(ExpirationResolution.DAY)) {
				Calendar from = Calendar.getInstance(Locale.GERMANY);
				from.set(Calendar.HOUR_OF_DAY, 0);
				from.set(Calendar.MINUTE, 0);
				from.set(Calendar.SECOND, 0);
				from.set(Calendar.MILLISECOND, 0);
				from.set(Calendar.DAY_OF_MONTH, 1);
				
				Calendar to = Calendar.getInstance(Locale.GERMANY);
				
				int lastDate = to.getActualMaximum(Calendar.DATE);
				to.set(Calendar.DATE, lastDate); 
				to.set(Calendar.HOUR_OF_DAY, 23);
				to.set(Calendar.MINUTE, 59);
				to.set(Calendar.SECOND, 59);
				to.set(Calendar.MILLISECOND, 999);
				
				
				int maxviews = def.getViewExpirations().get(ExpirationResolution.MONTH);
				long impressions = RuntimeContext.getTrackService().count(new Criterion(Criterion.Criteria.Banner, banner.getId()), EventType.IMPRESSION, from.getTime(), to.getTime());
				
				if (impressions <= maxviews) {
					return true;
				}
			}
		} catch (ServiceException e) {
			logger.error("", e);
		}
		
		return false;
	}

}

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
package net.mad.ads.server.utils.selection.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.ViewExpirationConditionDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.enums.ExpirationResolution;
import net.mad.ads.server.utils.RuntimeContext;
import net.mad.ads.server.utils.context.AdContext;
import net.mad.ads.server.utils.selection.AdSelector;
import net.mad.ads.services.tracking.Criterion;
import net.mad.ads.services.tracking.events.EventType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Das Banner wird aufgrund der prozentualen Anzeige-Häufigkeit ausgewählt.
 * 
 * 1. ermitteln, wie viel Prozent der Impressions schon erreicht wurde
 * 2. das Banner wählen, das prozentual am wenigsten angezeigt wurde
 * 
 * Verhalten für Banner ohne Beschränkung der Impressionen:
 * 1. der kleinste ermittelte Prozentwert wird verwendet
 * 
 * @author thmarx
 * 
 * TODO: Dieser AdSelector sollte mal überarbeitet und erleichtert werden.
 *
 */
public class ImpressionPercentageSingleAdSelector implements AdSelector {
	
	private static final Logger logger = LoggerFactory.getLogger(ImpressionPercentageSingleAdSelector.class);

	private static final AdSelector RANDOM_SELECTOR = new RandomSingleAdSelector();
	
	@Override
	public AdDefinition selectBanner(List<AdDefinition> banners, AdContext context) {

		List<BannerDecorator> decorators = new ArrayList<ImpressionPercentageSingleAdSelector.BannerDecorator>();
		for (AdDefinition def : banners) {
			decorators.add(new BannerDecorator(def));
		}
		
		// Sortieren
		Collections.sort(decorators, new Comparator<BannerDecorator>() {

			@Override
			public int compare(BannerDecorator o1, BannerDecorator o2) {
				float p1 = getPercentage(o1);
				float p2 = getPercentage(o2);
				
				o1.setPercentage(p1);
				o2.setPercentage(p2);
				
				if (p1 > p2) {
					return -1;
				} else if (p2 > p1) {
					return 1;
				}
				return 0;
			}

			
		});
		Collections.reverse(decorators);
		
		/*
		 * Alle Banner ermittel die in Frage kommen, also die, die noch nicht so häufig angezeigt wurden
		 *  
		 */
		List<AdDefinition> bannerList = new ArrayList<AdDefinition>();
		
		/*
		 * the smallest percentage view value used to select banner to choose the delivered banner
		 * 
		 *  The to steps to detect the smallest value and select all possible banners can be done in one step
		 *  because the list of banners is ordered by the view-percentage
		 */
		float sma = -2f;
		for (BannerDecorator bd : decorators) {
			// Every banner without expiration has -1 as percentage
			if (bd.getPercentage() == -1f) {
				// these banners are added
				bannerList.add(bd.getBanner());
			} else if (sma == -2f) {
				// first banner with view expiration is used as minimum -> add
				sma = bd.getPercentage();
				bannerList.add(bd.getBanner());
			} else if (bd.getPercentage() <= sma) {
				// take all banners smaller or equals to the minimum -> add
				bannerList.add(bd.getBanner());
			} else if (bd.getPercentage() > sma) {
				// reached the bigger banners -> finish
				break;
			}
		}

		return RANDOM_SELECTOR.selectBanner(bannerList, context);
	}
	
	private float getPercentage(BannerDecorator decorator) {
		try {
			AdDefinition o1 = decorator.getBanner();
			if (decorator.hasPercentage()) {
				return decorator.getPercentage();
			}
			if (o1.hasConditionDefinition(ConditionDefinitions.VIEW_EXPIRATION)) {
				ViewExpirationConditionDefinition def = (ViewExpirationConditionDefinition) o1.getConditionDefinition(ConditionDefinitions.VIEW_EXPIRATION);
				if (def.getViewExpirations().containsKey(ExpirationResolution.DAY)) {
	                Calendar from = Calendar.getInstance(Locale.GERMANY);
	                from.set(Calendar.HOUR_OF_DAY, 0);
	                from.set(Calendar.MINUTE, 0);
	                from.set(Calendar.SECOND, 0);
	                from.set(Calendar.MILLISECOND, 0);
	                
	                Calendar to = Calendar.getInstance(Locale.GERMANY);
	                to.set(Calendar.HOUR_OF_DAY, 0);
	                to.set(Calendar.MINUTE, 0);
	                to.set(Calendar.SECOND, 0);
	                to.set(Calendar.MILLISECOND, 0);
	                to.add(Calendar.DAY_OF_WEEK, 1);
					
					int maxViewCount = def.getViewExpirations().get(ExpirationResolution.DAY);
					long viewCount = RuntimeContext.getTrackService().count(new Criterion(Criterion.Criteria.Banner, o1.getId()), EventType.IMPRESSION, from.getTime(), to.getTime());
					
					float percent = 0.0f;
					if (maxViewCount > 0) {
						percent = (float)viewCount / (float)maxViewCount;
					}
					return percent;
				} else {
					return -1;
				}
			} else {
				return -1;
			}
			
		} catch (Exception e) {
			logger.error("", e);
		}
		return 0.0f;
	}
	
	class BannerDecorator {
		private AdDefinition banner = null;
		private float percentage = -1.0f;
		
		public BannerDecorator (AdDefinition banner) {
			this.banner = banner;
		}

		public float getPercentage() {
			return percentage;
		}

		public void setPercentage(float percentage) {
			this.percentage = percentage;
		}

		public AdDefinition getBanner() {
			return banner;
		}
		
		public boolean hasPercentage () {
			return percentage != -1f;
		}
	}

}

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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mad.ads.base.utils.BaseContext;
import net.mad.ads.base.utils.exception.ServiceException;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.definition.condition.ViewExpirationConditionDefinition;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.ConditionDefinitions;
import net.mad.ads.db.enums.ExpirationResolution;
import net.mad.ads.server.utils.RuntimeContext;
import net.mad.ads.services.tracking.Criterion;
import net.mad.ads.services.tracking.TrackingService;
import net.mad.ads.services.tracking.events.EventType;
import net.mad.ads.services.tracking.events.TrackEvent;

import org.junit.BeforeClass;
import org.junit.Test;

public class ImpressionPercentageSingleAdSelectorTest {

	private static ImpressionPercentageSingleAdSelector SELECTOR = new ImpressionPercentageSingleAdSelector();
	private static List<AdDefinition> banners = new ArrayList<AdDefinition>();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		RuntimeContext.setTrackService(new TrackingService() {
			@Override
			public void track(TrackEvent event) throws ServiceException {}
			@Override
			public void open(BaseContext context) throws ServiceException {}
			@Override
			public List<TrackEvent> list(Criterion criterion, EventType type,
					Date from, Date to) throws ServiceException { return null; }
			@Override
			public void delete(Criterion criterion, Date from, Date to)
					throws ServiceException {}

			@Override
			public void close() throws ServiceException {}
			@Override
			public void clear(Criterion criterion) throws ServiceException {}
			
			@Override
			public long count(Criterion criterion, EventType type, Date from, Date to)
					throws ServiceException {
				String bannerID = criterion.value;
				if (bannerID.equals("1")) {
					return 10;
				} else if (bannerID.equals("2")) {
					return 20;
				} else if (bannerID.equals("3")) {
					return 30;
				} else if (bannerID.equals("4")) {
					return 40;
				} else if (bannerID.equals("5")) {
					return 40;
				}
				
				return 0;
			}
		});
		for (int i = 1; i <= 4; i++) {
			AdDefinition b = new ImageAdDefinition();
			b.setId("" + i);
			ViewExpirationConditionDefinition vdef = new ViewExpirationConditionDefinition();
			vdef.getViewExpirations().put(ExpirationResolution.DAY, 100);
			b.addConditionDefinition(ConditionDefinitions.VIEW_EXPIRATION, vdef);
			
			banners.add(b);
		}
		AdDefinition b = new ImageAdDefinition();
		b.setId("5");
		banners.add(b);
		
	}

	@Test
	public void testSelectBanner() {
		AdDefinition b = SELECTOR.selectBanner(banners, null);
		assertTrue("wrong banner selected", (b.getId().equals("1") || b.getId().equals("5")));
	}

}

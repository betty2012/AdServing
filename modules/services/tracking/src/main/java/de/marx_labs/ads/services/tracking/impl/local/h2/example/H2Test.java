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
package de.marx_labs.ads.services.tracking.impl.local.h2.example;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import de.marx_labs.ads.base.utils.BaseContext;
import de.marx_labs.ads.base.utils.exception.ServiceException;
import de.marx_labs.ads.base.utils.utils.DateHelper;
import de.marx_labs.ads.services.tracking.Criterion;
import de.marx_labs.ads.services.tracking.TrackingContextKeys;
import de.marx_labs.ads.services.tracking.TrackingService;
import de.marx_labs.ads.services.tracking.events.ClickTrackEvent;
import de.marx_labs.ads.services.tracking.events.EventAttribute;
import de.marx_labs.ads.services.tracking.events.EventType;
import de.marx_labs.ads.services.tracking.events.ImpressionTrackEvent;
import de.marx_labs.ads.services.tracking.events.TrackEvent;
import de.marx_labs.ads.services.tracking.impl.local.h2.H2TrackingService;

import org.h2.jdbcx.JdbcDataSource;

/**
 * 
 * @author tmarx
 */
public class H2Test {
	public static void main(String[] args) throws Exception {
		
		long before = System.currentTimeMillis();

		JdbcDataSource ds = new JdbcDataSource();
//		ds.setURL("jdbc:h2:mem:test");
		ds.setURL("jdbc:h2:file:/tmp/test_h2");
		ds.setUser("sa");
		ds.setPassword("sa");

		BaseContext context = new BaseContext();
		context.put(TrackingContextKeys.EMBEDDED_TRACKING_DATASOURCE, ds);

		TrackingService ts = new H2TrackingService();

		ts.open(context);

		printTime("Opening TrackingService", before);
		
		before = System.currentTimeMillis();
		
		for (int i = 0; i < 10000; i++) {
			boolean click = false;
			if (i % 3 == 0) {
				click = true;
			}
			trackData(ts, i, UUID.randomUUID().toString(), click);
		}
		
		printTime("Save TrackEvents", before);

		Calendar from = Calendar.getInstance(Locale.GERMANY);
		from.set(Calendar.MONTH, Calendar.JANUARY);
		from.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		from.set(Calendar.HOUR_OF_DAY, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);

		Calendar to = Calendar.getInstance(Locale.GERMANY);
		to.set(Calendar.MONTH, Calendar.JANUARY);
		to.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		// to.set(Calendar.HOUR_OF_DAY, 23);
		// to.set(Calendar.MINUTE, 59);
		// to.set(Calendar.SECOND, 59);
		// to.set(Calendar.MILLISECOND, 999);
		to.set(Calendar.HOUR_OF_DAY, 0);
		to.set(Calendar.MINUTE, 0);
		to.set(Calendar.SECOND, 0);
		to.set(Calendar.MILLISECOND, 0);
		to.add(Calendar.DAY_OF_WEEK, 1);

		// System.out.println();
		// System.out.println(DateHelper.format(from.getTime()));
		// System.out.println(DateHelper.format(to.getTime()));
		// System.out.println();

		// System.out.println(ts.countClicks("b1", from.getTime(),
		// to.getTime()));
		// System.out.println(ts.countImpressions("b1", from.getTime(),
		// to.getTime()));

		before = System.currentTimeMillis();
		
		List<TrackEvent> list = ts.list(new Criterion(Criterion.Criteria.Site,
				"demo Site"), null, from.getTime(), to.getTime());
		System.out.println(list.size());
		printTime("List TrackEvents", before);
		
		before = System.currentTimeMillis();
		
		System.out.println(ts.count(new Criterion(Criterion.Criteria.Banner,
				"b1"), EventType.CLICK, from.getTime(), to.getTime()));
		
		printTime("count click TrackEvents", before);
		
		before = System.currentTimeMillis();
		
		System.out.println(ts.count(new Criterion(Criterion.Criteria.Banner,
				"b1"), EventType.IMPRESSION, from.getTime(), to.getTime()));

		printTime("count impression TrackEvents", before);
		
		ts.close();
	}

	private static void printTime (String message, long start) {
		System.out.println(message + " : " + (System.currentTimeMillis() - start));
	}

	private static void trackData(TrackingService ts, int index, String id, boolean click) throws ServiceException {
		Calendar created = Calendar.getInstance(Locale.GERMANY);
		created.set(Calendar.MONTH, Calendar.JANUARY);
		created.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		created.set(Calendar.MINUTE, 0);
		created.set(Calendar.HOUR_OF_DAY, 0);
		created.set(Calendar.SECOND, 0);
		
		created.add(Calendar.SECOND, index);

		TrackEvent event = null;
		if (click) {
			event = new ClickTrackEvent();
		} else {
			event = new ImpressionTrackEvent();
		}
		event.put(EventAttribute.TIME, DateHelper.format(created.getTime()));
		event.setTime(created.getTime().getTime());
		event.setCampaign("c1");
		event.setSite("demo Site");
		event.put(EventAttribute.AD_ID, "b1");
		event.setUser("user1");
		event.setId(id);
		event.setIp("ip1");
		ts.track(event);

		/*
		created.add(Calendar.HOUR_OF_DAY, 1);
		event = new ClickTrackEvent();
		event.setId("id 2");
		event.setCampaign("c1");
		event.setIp("ip1");
		event.put(EventAttribute.TIME, DateHelper.format(created.getTime()));
		event.setTime(created.getTime().getTime());
		// System.out.println(DateHelper.format(created.getTime()));
		event.setSite("demo Site");
		event.put(EventAttribute.BANNER_ID, "b1");
		event.setUser("user1");
		ts.track(event);

		created.add(Calendar.HOUR_OF_DAY, 1);
		event = new ImpressionTrackEvent();
		event.setId("id 3");
		event.setIp("ip1");
		event.setCampaign("c1");
		event.put(EventAttribute.TIME, DateHelper.format(created.getTime()));
		event.setTime(created.getTime().getTime());
		// System.out.println(DateHelper.format(created.getTime()));
		event.setSite("demo Site");
		event.put(EventAttribute.BANNER_ID, "b1");
		event.setUser("user1");
		ts.track(event);
		event.setUser("user2");
		event.setId("id 4");
		ts.track(event);
		*/
	}
}

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
package net.mad.ads.services.tracking.impl.local.h2.example;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import net.mad.ads.base.utils.BaseContext;
import net.mad.ads.base.utils.exception.ServiceException;
import net.mad.ads.base.utils.utils.DateHelper;
import net.mad.ads.services.tracking.Criterion;
import net.mad.ads.services.tracking.TrackingContextKeys;
import net.mad.ads.services.tracking.TrackingService;
import net.mad.ads.services.tracking.events.ClickTrackEvent;
import net.mad.ads.services.tracking.events.EventAttribute;
import net.mad.ads.services.tracking.events.EventType;
import net.mad.ads.services.tracking.events.ImpressionTrackEvent;
import net.mad.ads.services.tracking.events.TrackEvent;
import net.mad.ads.services.tracking.impl.local.h2.HSQLDBTrackingService;

import org.hsqldb.jdbc.pool.JDBCPooledDataSource;

/**
 * 
 * @author tmarx
 */
public class HSQLDBTest {
	public static void main(String[] args) throws Exception {

		
		long before = System.currentTimeMillis();
		
		JDBCPooledDataSource ds = new JDBCPooledDataSource();
//		ds.setUrl("jdbc:hsqldb:mem:test");
		ds.setUrl("jdbc:hsqldb:file:/tmp/test_hsqldb");
		ds.setUser("sa");
		ds.setPassword("sa");

		BaseContext context = new BaseContext();
		context.put(TrackingContextKeys.EMBEDDED_TRACKING_DATASOURCE, ds);

		TrackingService ts = new HSQLDBTrackingService();

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

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
package de.marx_labs.ads.services.tracking.impl.mongo;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.marx_labs.ads.base.utils.BaseContext;
import de.marx_labs.ads.services.tracking.Criterion;
import de.marx_labs.ads.services.tracking.Criterion.Criteria;
import de.marx_labs.ads.services.tracking.events.ClickTrackEvent;
import de.marx_labs.ads.services.tracking.events.EventType;
import de.marx_labs.ads.services.tracking.events.ImpressionTrackEvent;
import de.marx_labs.ads.services.tracking.events.TrackEvent;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.foursquare.fongo.Fongo;
import com.mongodb.DBCollection;

public class MongoTrackingServiceTest {
	private static MongoTrackingService service = null;
	private static DBCollection collection = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BaseContext context = new BaseContext();
		
		Fongo fongo = new Fongo("mongo server 1");
		context.put(MongoTrackingService.MONGO_DB, fongo.getDB("tracking"));
		collection = fongo.getDB("tracking").getCollection(MongoTrackingService.MONGO_DB_COLLECTION);
		
		service = new MongoTrackingService();
		service.open(context);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void test_1() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.MONTH, Calendar.AUGUST);
		cal.set(Calendar.YEAR, 1978);
		
		int clicks = 0;
		int views = 0;
		for (int i = 0; i < 30; i++) {
			if (i > 0) {
				cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
			}
			TrackEvent event = null;
			if (i % 2 == 0) {
				event = new ClickTrackEvent();
				clicks++;
				event.setBannerId("1");
				event.setCampaign("camp 1");
				event.setSite("site 1");
			} else {
				event = new ImpressionTrackEvent();
				views++;
				event.setBannerId("2");
				event.setCampaign("camp 2");
				event.setSite("site 2");
			}
			event.setIp("127.0.0.1");
			
			event.setId(UUID.randomUUID().toString());
			event.setTime(cal.getTimeInMillis());
			event.setUser(UUID.randomUUID().toString());
			
			service.track(event);
		}
		
		long count = service.count(null, null, null, null);
		assertEquals((clicks + views), count);
		
		count = service.count(null, EventType.CLICK, null, null);
		assertEquals(clicks, count);
		
		count = service.count(null, EventType.IMPRESSION, null, null);
		assertEquals(views, count);
		
		Criterion criterion = new Criterion(Criteria.Banner, "1");
		List<TrackEvent> events = service.list(criterion, null, null, null);
		assertEquals(clicks, events.size());
		
		criterion = new Criterion(Criteria.Banner, "2");
		events = service.list(criterion, null, null, null);
		assertEquals(views, events.size());
		
		events = service.list(criterion, null, new Date(), new Date());
		assertEquals(0, events.size());
		
		System.out.println("range test");
		
		Calendar from = Calendar.getInstance();
		from.set(Calendar.DAY_OF_MONTH, 1);
		from.set(Calendar.MONTH, Calendar.AUGUST);
		from.set(Calendar.YEAR, 1977);
		from.set(Calendar.HOUR_OF_DAY, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);
		
		Calendar to = Calendar.getInstance();
		to.set(Calendar.DAY_OF_MONTH, 1);
		to.set(Calendar.MONTH, Calendar.SEPTEMBER);
		to.set(Calendar.YEAR, 1977);
		to.set(Calendar.HOUR_OF_DAY, 23);
		to.set(Calendar.MINUTE, 59);
		to.set(Calendar.SECOND, 59);
		to.set(Calendar.MILLISECOND, 999);
		
		events = service.list(null, null, from.getTime(), to.getTime());
		assertEquals(0, events.size());
		
		from.set(1978, Calendar.AUGUST, 1);
		to.set(1978, Calendar.AUGUST, 30);
		
		events = service.list(null, null, from.getTime(), to.getTime());
		assertEquals(30, events.size());
	}


}

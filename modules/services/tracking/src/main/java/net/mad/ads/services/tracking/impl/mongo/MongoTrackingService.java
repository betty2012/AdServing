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
package net.mad.ads.services.tracking.impl.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mad.ads.base.utils.BaseContext;
import net.mad.ads.base.utils.exception.ServiceException;
import net.mad.ads.services.tracking.Criterion;
import net.mad.ads.services.tracking.TrackingService;
import net.mad.ads.services.tracking.events.ClickTrackEvent;
import net.mad.ads.services.tracking.events.EventAttribute;
import net.mad.ads.services.tracking.events.EventType;
import net.mad.ads.services.tracking.events.ImpressionTrackEvent;
import net.mad.ads.services.tracking.events.TrackEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoTrackingService implements TrackingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoTrackingService.class);
	
	public static final String MONGO_DB = "mongo_db";
	public static final String MONGO_DB_COLLECTION = "mongo_db_collection";
	private DB db;
	private DBCollection collection;

	@Override
	public void open(BaseContext context) throws ServiceException {
		db = (DB) context.get(MONGO_DB);
		collection = db.getCollection(MONGO_DB_COLLECTION);
	}

	@Override
	public void close() throws ServiceException {
		// nothing to do
	}

	private static BasicDBObject toDB(TrackEvent event) {
		BasicDBObject bson = new BasicDBObject();

		bson.put("timestamp", event.getTime());
		bson.put("type", event.getType().getName());
		for (EventAttribute attr : event.keySet()) {
			String name = attr.getName();
			String value = event.get(attr);
			bson.put(name, value);
		}

		return bson;
	}

	public static TrackEvent fromDB(DBObject bson) {
		String type = (String) bson.get("type");
		EventType eType = EventType.forName(type);

		TrackEvent event = null;

		if (eType.equals(EventType.CLICK)) {
			event = new ClickTrackEvent();
		} else {
			event = new ImpressionTrackEvent();
		}

		event.setTime((Long) bson.get("timestamp"));

		for (EventAttribute attr : EventAttribute.values()) {
			event.put(attr, (String) bson.get(attr.getName()));
		}

		return event;
	}

	@Override
	public void track(TrackEvent event) throws ServiceException {
		collection.save(toDB(event));

	}

	@Override
	public List<TrackEvent> list(Criterion criterion, EventType type,
			Date from, Date to) throws ServiceException {
		DBObject query = getQuery(criterion, type, from, to);

		DBCursor cur = collection.find(query);
		List<TrackEvent> resultCollection = new ArrayList<TrackEvent>();
		try {
			while (cur.hasNext()) {
				TrackEvent event = fromDB(cur.next());
				resultCollection.add(event);
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}

		return resultCollection;
	}

	@Override
	public long count(Criterion criterion, EventType type, Date from, Date to)
			throws ServiceException {
		DBObject query = getQuery(criterion, type, from, to);

		return collection.count(query);
	}

	@Override
	public void delete(Criterion criterion, Date from, Date to)
			throws ServiceException {
		DBObject query = getQuery(criterion, null, from, to);

		collection.remove(query);
	}

	@Override
	public void clear(Criterion criterion) throws ServiceException {
		DBObject query = getQuery(criterion, null, null, null);

		collection.remove(query);
	}

	private DBObject getQuery(Criterion criterion, EventType type, Date from,
			Date to) {
		BasicDBObject queryObject = new BasicDBObject();

		if (from != null || to != null) {
			BasicDBObject predicate = new BasicDBObject();
			if (from != null) {
				predicate.put("$gte", from.getTime());
			}
			if (to != null) {
				predicate.put("$lte", to.getTime());
			}
			
			queryObject.put("timestamp", predicate);
		}

		if (type != null) {
			queryObject.put("type", type.getName());
		}

		if (criterion != null) {
			switch (criterion.criterion) {
			case Banner:
				queryObject
						.put(EventAttribute.AD_ID.getName(), criterion.value);
				break;
			case Campaign:
				queryObject.put(EventAttribute.CAMPAIGN.getName(),
						criterion.value);
				break;
			case Site:
				queryObject.put(EventAttribute.SITE.getName(), criterion.value);
				break;
			}
		}

		return queryObject;
	}
}

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
package net.mad.ads.db.db.store.impl;


import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.mad.ads.db.AdDBConstants;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.store.AdDBStore;
import net.mad.ads.db.definition.AdDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.mapdb.*;



public class AdDBMongoStore implements AdDBStore {

	private static final Logger logger = LoggerFactory.getLogger(AdDBMongoStore.class);
	
	private AdDB addb = null;

	public static final String STORE_COLLECTION = "store.collection";

	private DBCollection storeCollection = null;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	public AdDBMongoStore(AdDB db) {
		this.addb = db;
	}
	
	@Override
	public void open() throws IOException {
		storeCollection = (DBCollection) addb.manager.getContext().configuration
				.get(STORE_COLLECTION);
	}

	@Override
	public void close() throws IOException {
		
	}

	@Override
	public void addBanner(AdDefinition banner) throws IOException {
		String json = mapper.writeValueAsString(banner);
		DBObject dbobject = (DBObject) JSON.parse(json);
		
		this.storeCollection.save(dbobject);
	}

	@Override
	public void deleteBanner(String id) throws IOException {
		this.storeCollection.remove(new BasicDBObject("id", id));
	}

	@Override
	public AdDefinition getBanner(String id) {
		
		DBObject dbobject = this.storeCollection.findOne(new BasicDBObject("id", id));
		try {
			return mapper.readValue(JSON.serialize(dbobject), AdDefinition.class);
		} catch (Exception e) {
			logger.error("error creating AdDefinition from json", e);
		}
		return null;
	}

	@Override
	public int size() {
		return (int) this.storeCollection.count();
	}

	@Override
	public void clear() {
		this.storeCollection.remove(new BasicDBObject());
	}

	@Override
	public void commit() throws IOException {
		
	}

	@Override
	public void rollback() {
		
	}

	@Override
	public void beginTransaction() throws IOException {
		// not necessary
	}

}

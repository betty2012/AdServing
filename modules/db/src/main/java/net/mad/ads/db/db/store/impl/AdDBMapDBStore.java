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

import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.store.AdDBStore;
import net.mad.ads.db.definition.AdDefinition;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;



public class AdDBMapDBStore implements AdDBStore {

	private static final Logger logger = LoggerFactory.getLogger(AdDBMapDBStore.class);
	
	private Map<String, AdDefinition> banners = null;
	
	private AdDB addb = null;

	private DB db;
	
	public AdDBMapDBStore(AdDB db) {
		this.addb = db;
	}
	
	@Override
	public void open() throws IOException {
		if (Strings.isNullOrEmpty(addb.manager.getContext().datadir)) {
			throw new IOException("temp directory can not be empty");
		}
		
		String dir = addb.manager.getContext().datadir;
		if (!dir.endsWith("/") || !dir.endsWith("\\")) {
			dir += "/";
		}
		File temp = new File(dir + "store");
		if (!temp.exists()) {
			temp.mkdirs();
		}
		
		db = DBMaker.newFileDB(new File(dir + "store/ads"))
			    .closeOnJvmShutdown()
			    .make();
		
		banners = db.getTreeMap("collectionName");
	}

	@Override
	public void close() throws IOException {
		if (db != null) {
            db.close();
            db = null;
        }
	}

	@Override
	public void addBanner(AdDefinition banner) throws IOException {
		this.banners.put(banner.getId(), banner);
		this.db.commit();
	}

	@Override
	public void deleteBanner(String id) throws IOException {
		this.banners.remove(id);
		this.db.commit();
	}

	@Override
	public AdDefinition getBanner(String id) {
		return this.banners.get(id);
	}

	@Override
	public int size() {
		return this.banners.size();
	}

	@Override
	public void clear() {
		this.banners.clear();
		this.db.commit();
	}

	@Override
	public void commit() throws IOException {
		this.db.commit();
	}

	@Override
	public void rollback() {
		this.db.rollback();
	}

	@Override
	public void beginTransaction() throws IOException {
		// not necessary
	}

}

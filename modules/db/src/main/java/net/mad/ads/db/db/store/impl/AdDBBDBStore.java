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
package net.mad.ads.db.db.store.impl;


import java.io.File;
import java.io.IOException;
import java.util.Map;

import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.store.AdDBStore;
import net.mad.ads.db.definition.AdDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.sleepycat.bind.serial.ClassCatalog;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.collections.StoredSortedMap;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;


public class AdDBBDBStore implements AdDBStore {

	private static final Logger logger = LoggerFactory.getLogger(AdDBBDBStore.class);
	
	private Database db;
	private ClassCatalog catalog;
    private Environment env;
	
	private Map<String, AdDefinition> banners = null;
	
	private AdDB addb = null;

	public AdDBBDBStore(AdDB db) {
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
		
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(false);
		envConfig.setAllowCreate(true);
		env = new Environment(temp, envConfig);
		/*
		 * the database should be persistence also after a readstart
		 */
//		try {
//			env.removeDatabase(null, "banner.db");
//		} catch (DatabaseNotFoundException e) {
//			logger.debug("no database exists");
//		}
		
		DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        Database catalogDb = env.openDatabase(null, "catalog", dbConfig);
        catalog = new StoredClassCatalog(catalogDb);
		
		StringBinding keyBinding = new StringBinding();
		SerialBinding<AdDefinition> valueBinding = new SerialBinding<AdDefinition>(catalog, AdDefinition.class);
		/* Open a data store. */
        
        this.db = env.openDatabase(null, "banner.db", dbConfig);

        /*
         * Now create a collection style map view of the data store so that it
         * is easy to work with the data in the database.
         */
        this.banners = new StoredSortedMap<String, AdDefinition>
            (db, keyBinding, valueBinding, true);
	}

	@Override
	public void close() throws IOException {
		if (catalog != null) {
            catalog.close();
            catalog = null;
        }
        if (db != null) {
            db.close();
            db = null;
        }
        if (env != null) {
            env.close();
            env = null;
        }
	}

	@Override
	public void addBanner(AdDefinition banner) throws IOException {
		this.banners.put(banner.getId(), banner);
	}

	@Override
	public void deleteBanner(String id) throws IOException {
		this.banners.remove(id);
	}

	@Override
	public AdDefinition getBanner(String id) {
		return this.banners.get(id);
	}

	@Override
	public int size() {
		return this.banners.size();
	}

}

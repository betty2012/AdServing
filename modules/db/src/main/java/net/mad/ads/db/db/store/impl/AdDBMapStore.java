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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.mad.ads.db.db.store.AdDBStore;
import net.mad.ads.db.definition.AdDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Map Store
 * 
 * Transactions are not supported
 * @author thmarx
 *
 */
public class AdDBMapStore implements AdDBStore {

	private static final Logger logger = LoggerFactory.getLogger(AdDBMapStore.class);
	
	private Map<String, AdDefinition> banners = null;
	
	@Override
	public void open() throws IOException {
		this.banners = new HashMap<String, AdDefinition>();
	}

	@Override
	public void close() throws IOException {
		this.banners = null;
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

	@Override
	public void clear() {
		this.banners.clear();
	}

	@Override
	public void commit() throws IOException {
		
	}

	@Override
	public void rollback() throws IOException {
		
	}

	@Override
	public void beginTransaction() throws IOException {
		
	}

}

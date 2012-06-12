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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.store.AdDBStore;
import net.mad.ads.db.definition.AdDefinition;

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

}

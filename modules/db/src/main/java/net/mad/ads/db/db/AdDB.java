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
package net.mad.ads.db.db;


import java.io.IOException;
import java.util.List;

import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.db.index.AdDBIndex;
import net.mad.ads.db.db.index.impl.AdDBLuceneIndex;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.db.store.AdDBStore;
import net.mad.ads.db.db.store.impl.AdDBBDBStore;
import net.mad.ads.db.db.store.impl.AdDBMapStore;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.utils.ConditionHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BDB - BannerDatenbank
 * 
 * 
 * @author thorsten
 */
public class AdDB {
	
	private static final Logger logger = LoggerFactory.getLogger(AdDB.class);
	
    private AdDBStore adStore;
    private AdDBIndex adIndex = null;
    
    public final AdDBManager manager;
    
	public AdDB(AdDBManager manager) {
		this.manager = manager;
	}

	public void open() throws IOException {
		adIndex = new AdDBLuceneIndex(this);
		adIndex.open();
		
		if (manager.getContext().useRamOnly) {
			adStore = new AdDBMapStore();
		} else {
			adStore = new AdDBBDBStore(this);
		}
		this.adStore.open();
	}
	
	public void reopen () throws IOException {
		adIndex.reopen();
	}
	
	public void close() throws IOException {
		this.adIndex.close();
		this.adStore.close();
	}
	
	/**
	 * Schreibt ein Banner in den Index und in die Datenbank
	 * 
	 * @param banner
	 * @throws IOException
	 */
	public void addBanner (AdDefinition banner) throws IOException {
		this.adIndex.addBanner(banner);
		this.adStore.addBanner(banner);
	}
	
	/**
	 * Löscht ein Banner aus dem Index und der Datenbank
	 * @param id
	 * @throws IOException
	 */
	public void deleteBanner (String id) throws IOException {
		this.adIndex.deleteBanner(id);
		this.adStore.deleteBanner(id);
	}
	
	/**
	 * Liefert eine Liste von BannerDefinitionen, die die Kriterien des Request erfüllen
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public List<AdDefinition> search (AdRequest request) throws IOException {
		List<AdDefinition> result = this.adIndex.search(request);
		
		/*
		 * Zu letzt wird das Ergebnis gefiltert
		 * 
		 * Grund dafür ist, dass wenn ein Banner nur in einem 5 KM Radius um
		 * eine Stadt angezeigt werden soll, das aktuell noch nicht über eine
		 * Query gemacht werden kann. Für dieses Dinge können Filter verwendet
		 * werden
		 */
		result = ConditionHelper.getInstance().processFilter(request, result, this);

		return result;
	}
	
	/**
	 * Liefert ein Banner für eine ID
	 * @param id Die ID des Banners
	 * @return
	 */
	public AdDefinition getBanner (String id) {
		return this.adStore.getBanner(id);
	}
	
	/**
	 * liefert die Anzahl der Banner in der Datenbank
	 * @return
	 */
	public int size () {		
		return this.adIndex.size();
	}
}

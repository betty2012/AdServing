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
package net.mad.ads.db.db;


import java.io.IOException;
import java.util.List;

import net.mad.ads.db.AdDBManager;
import net.mad.ads.db.db.index.AdDBIndex;
import net.mad.ads.db.db.index.impl.AdDBLuceneIndex;
import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.db.store.AdDBStore;
import net.mad.ads.db.db.store.impl.AdDBBDBStore;
import net.mad.ads.db.db.store.impl.AdDBMapDBStore;
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
//			adStore = new AdDBBDBStore(this);
			adStore = new AdDBMapDBStore(this);
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

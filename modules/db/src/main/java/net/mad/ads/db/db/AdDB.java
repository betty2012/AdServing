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

import net.mad.ads.db.db.request.AdRequest;
import net.mad.ads.db.db.store.AdStore;
import net.mad.ads.db.db.store.impl.local.LocalAdStore;
import net.mad.ads.db.db.store.impl.remote.RemoteAdStore;
import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.db.enums.Mode;
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
	
//    private AdDBStore adStore;
//    private AdDBIndex adIndex = null;
	private AdStore store;
    
    public final AdDBManager manager;
    
	public AdDB(AdDBManager manager) {
		this.manager = manager;
	}

	public void open() throws IOException {
		
		if (manager.getContext().mode.equals(Mode.REMOTE)) {
			this.store = new RemoteAdStore(this);
		} else if (manager.getContext().mode.equals(Mode.LOCAL)) {
			store = new LocalAdStore(this);
		} else if (manager.getContext().mode.equals(Mode.MEMORY)) {
			store = new LocalAdStore(this, true);
		} 
		
		
		store.open();
	}
	
	public void reopen () throws IOException {
		store.reopen();
	}
	
	public void clear () throws IOException {
		store.clear();
	}
	
	public void close() throws IOException {
		this.store.close();
	}
	
	/**
	 * Schreibt ein Banner in den Index und in die Datenbank
	 * 
	 * @param banner
	 * @throws IOException
	 */
	public void addBanner (AdDefinition banner) throws IOException {
		store.add(banner);
	}
	
	/**
	 * Löscht ein Banner aus dem Index und der Datenbank
	 * @param id
	 * @throws IOException
	 */
	public void deleteBanner (String id) throws IOException {
		store.delete(id);
	}
	
	/**
	 * Liefert eine Liste von BannerDefinitionen, die die Kriterien des Request erfüllen
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public List<AdDefinition> search (AdRequest request) throws IOException {
		List<AdDefinition> result = this.store.search(request);
		
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
		return this.store.get(id);
	}
	
	/**
	 * liefert die Anzahl der Banner in der Datenbank
	 * @return
	 */
	public int size () {		
		return this.store.size();
	}
}

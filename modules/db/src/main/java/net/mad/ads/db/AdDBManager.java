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
package net.mad.ads.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanQuery;

import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.condition.impl.AdSlotCondition;
import net.mad.ads.db.condition.impl.CountryCondition;
import net.mad.ads.db.condition.impl.DateCondition;
import net.mad.ads.db.condition.impl.DayCondition;
import net.mad.ads.db.condition.impl.DistanceCondition;
import net.mad.ads.db.condition.impl.ExcludeSiteCondition;
import net.mad.ads.db.condition.impl.KeyValueCondition;
import net.mad.ads.db.condition.impl.KeywordCondition;
import net.mad.ads.db.condition.impl.SiteCondition;
import net.mad.ads.db.condition.impl.StateCondition;
import net.mad.ads.db.condition.impl.TimeCondition;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.nonblocking.NonBlockingAdDB;

public class AdDBManager {

	private final AdDB adDB;
	
	private final AdDBContext context = new AdDBContext();
	
	private final List<Condition<Document, BooleanQuery>> conditions = new ArrayList<Condition<Document, BooleanQuery>>();
	
	private ExecutorService executorService = null;
	
	public static Builder builder () {
		return new Builder();
	}
	
	private AdDBManager (boolean blocking, boolean closeExecutorService) {
		if (blocking) {
			this.adDB = new AdDB(this);
		} else {
			this.adDB = new NonBlockingAdDB(this);
		}
		
		// Default Conditions 
		conditions.add(new CountryCondition());
		conditions.add(new StateCondition());
		conditions.add(new DateCondition());
		conditions.add(new DayCondition());
		conditions.add(new TimeCondition());
		conditions.add(new KeywordCondition());
		conditions.add(new KeyValueCondition(this.adDB));
		conditions.add(new SiteCondition());
		conditions.add(new AdSlotCondition());
		conditions.add(new ExcludeSiteCondition());
		conditions.add(new DistanceCondition());
		/*
		 * should the executor service be closed after shutdown
		 * 
		 * maybe it is managed outside of the AdDBManager so it should not be closed here
		 */
		if (executorService != null && closeExecutorService) {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					executorService.shutdownNow();
				}
			});
		}
	}
	
	public ExecutorService getExecutorService () {
		return this.executorService;
	}
	
	public AdDB getAdDB () {
		return adDB;
	}
	
	public List<Condition<Document, BooleanQuery>> getConditions () {
		return conditions;
	}
	
	public AdDBContext getContext () {
		return context;
	}
	
	static public class Builder {
		private boolean blocking = true;
		private ExecutorService executorService = null;
		private boolean closeExecutorService = false;
		
		private Builder () {
			
		}
		
		public Builder blocking (boolean blocking) {
			this.blocking = blocking;
			return this;
		}
		
		public Builder closeExecutorService (boolean closeExecutorService) {
			this.closeExecutorService = closeExecutorService;
			return this;
		}
		
		public Builder executorService (ExecutorService executorService) {
			this.executorService = executorService;
			return this;
		}
		
		public AdDBManager build () {
			AdDBManager manager = new AdDBManager(blocking, closeExecutorService);
			if (!blocking && executorService == null) {
				executorService = Executors.newFixedThreadPool(1);
			}
			manager.executorService = executorService;
			
			return manager;
		}
	}
}

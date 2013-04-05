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

import net.mad.ads.db.condition.Condition;
import net.mad.ads.db.db.AdDB;
import net.mad.ads.db.db.nonblocking.NonBlockingAdDB;
import net.mad.ads.db.enums.Mode;

public class AdDBManager {

	private final AdDB adDB;
	
	private final AdDBContext context = new AdDBContext();
	
	private final List<Condition<?, ?>> conditions = new ArrayList<Condition<?, ?>>();
	
	private ExecutorService executorService = null;
	
	public static Builder builder () {
		return new Builder();
	}
	
	private AdDBManager (Mode mode, boolean blocking, boolean closeExecutorService) {
		if (blocking) {
			this.adDB = new AdDB(this);
		} else {
			this.adDB = new NonBlockingAdDB(this);
		}
		this.context.mode = mode;
		
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
	
	public List<Condition<?, ?>> getConditions () {
		return conditions;
	}
	
	public AdDBContext getContext () {
		return context;
	}
	
	static public class Builder {
		private boolean blocking = true;
		private ExecutorService executorService = null;
		private boolean closeExecutorService = false;
		private Mode mode = Mode.LUCENE;
		
		private Builder () {
			
		}
		
		public Builder blocking (boolean blocking) {
			this.blocking = blocking;
			return this;
		}
		public Builder mode (Mode mode) {
			this.mode = mode;
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
			AdDBManager manager = new AdDBManager(mode, blocking, closeExecutorService);
			
			
			// Default Conditions
			if (mode.equals(Mode.LUCENE)) {
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.CountryCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.StateCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.DateCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.DayCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.TimeCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.KeywordCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.KeyValueCondition(manager.adDB));
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.SiteCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.AdSlotCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.ExcludeSiteCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.lucene.DistanceCondition());
			} else if (mode.equals(Mode.MONGO)) {
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.CountryCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.StateCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.DateCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.DayCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.TimeCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.KeywordCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.KeyValueCondition(manager.adDB));
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.SiteCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.AdSlotCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.ExcludeSiteCondition());
				manager.conditions.add(new net.mad.ads.db.condition.impl.mongo.DistanceCondition());
			}
			
			if (!blocking && executorService == null) {
				executorService = Executors.newFixedThreadPool(1);
			}
			manager.executorService = executorService;
			
			return manager;
		}
	}
}

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
package de.marx_labs.ads.server.utils.cluster;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.server.utils.RuntimeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

/**
 * The Clustermanager 
 * 
 * 1. Update the AdDb after starting the AdServer
 * 2. Listen for changes and update the AdDb
 * 
 * @author marx
 *
 */
public class ClusterManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterManager.class);
	
	private boolean updateRunning = false;
	
	private HazelcastInstance instance = null;
	private IMap<String, AdDefinition> ads = null;
	
	private final ExecutorService executor;
	
	public ClusterManager () {
		executor = new PausableThreadPoolExecutor(1);
        instance = Hazelcast.newHazelcastInstance();
        ads = instance.getMap("ads");
        ads.addEntryListener(new AdEntryListener(), true);
	}
	
	/**
	 * init should be called directly after starting the AdServer
	 * in this step all AdDefinitions are loaded and added to the database
	 * 
	 * this step should only run once
	 */
	public void init () {
		LOGGER.debug("running initial database fillup");
		updateRunning = true;
		try {
			LOGGER.debug("found " + ads.size() + " AdDefinitions");
			for (AdDefinition ad : ads.values()) {
				try {
					RuntimeContext.getAdDB().deleteBanner(ad.getId());
					RuntimeContext.getAdDB().addBanner(ad);
				} catch (IOException e) {
					LOGGER.error("error updating AdDefinition " + ad.getId(), e);
				}		
			}
		} finally {
			updateRunning = false;
		}
	}
	
	public boolean isUpdating () {
		return this.updateRunning;
	}

	
	class AdEntryListener implements EntryListener<String, AdDefinition> {

		@Override
		public void entryAdded(EntryEvent<String, AdDefinition> entry) {
			executor.submit(new AdTaskRunnable(new AdTask(entry.getValue(), entry.getKey(), false)));
		}

		@Override
		public void entryEvicted(EntryEvent<String, AdDefinition> entry) {
			executor.submit(new AdTaskRunnable(new AdTask(entry.getValue(), entry.getKey(), true)));
		}

		@Override
		public void entryRemoved(EntryEvent<String, AdDefinition> entry) {
			executor.submit(new AdTaskRunnable(new AdTask(entry.getValue(), entry.getKey(), true)));
		}

		@Override
		public void entryUpdated(EntryEvent<String, AdDefinition> entry) {
			executor.submit(new AdTaskRunnable(new AdTask(entry.getValue(), entry.getKey(), false)));
		}	
	}
	
	static class AdTask {
		public final AdDefinition ad;
		public final String adid;
		public final boolean remove;
		public AdTask (AdDefinition ad, String adid, boolean remove) {
			this.ad = ad;
			this.adid = adid;
			this.remove = remove;
		}
		
		@Override
		public String toString() {
			if (ad != null) {
				return "AdDefinition" + ad.getId();
			}
			return "AdDefinition" + adid;
		}
	}
	
	static class AdTaskRunnable implements Runnable {

		private AdTask task;
		
		public AdTaskRunnable (AdTask task) {
			this.task = task;
		}
		
		@Override
		public void run() {
			if (RuntimeContext.getClusterManager().isUpdating()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LOGGER.error("error while waiting");
				}
			}
			try {
				if (task.remove && task.adid != null) {
					LOGGER.info("remove AdDefinition " + task.adid);
					RuntimeContext.getAdDB().deleteBanner(task.adid);
				} else if (task.ad != null) {
					RuntimeContext.getAdDB().deleteBanner(task.ad.getId());
					RuntimeContext.getAdDB().addBanner(task.ad);
					
					LOGGER.info("add AdDefinition " + task.ad.getId());
				}
				
				
			} catch (IOException e) {
				LOGGER.error("error updating AdDefinition " + (task.toString()), e);
			}
		}
		
	}
}

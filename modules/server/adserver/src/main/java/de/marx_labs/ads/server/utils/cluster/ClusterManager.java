/**
 * Mad-Advertisement
 * Copyright (C) 2011-2013 Thorsten Marx <thmarx@gmx.net>
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.server.utils.RuntimeContext;

/**
 * The Clustermanager
 * 
 * 1. Update the AdDb after starting the AdServer 2. Listen for changes and
 * update the AdDb
 * 
 * @author marx
 * 
 */
public class ClusterManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ClusterManager.class);

	private boolean updateRunning = false;
	private long updateStarted = 0l;

	private HazelcastInstance instance = null;
	private IMap<String, AdDefinition> ads = null;

	private final ExecutorService executor;

	public ClusterManager() {
		executor = new PausableThreadPoolExecutor((int) Math.round(Runtime
				.getRuntime().availableProcessors() * .75));
		instance = Hazelcast.newHazelcastInstance();
		ads = instance.getMap("ads");
		ads.addEntryListener(new AdEntryListener(this), true);
	}

	/**
	 * init should be called directly after starting the AdServer in this step
	 * all AdDefinitions are loaded and added to the database
	 * 
	 * this step should only run once
	 */
	public void init() {
		LOGGER.debug("running initial database fillup");
		updateRunning = true;
		updateStarted = System.currentTimeMillis();
		try {
			LOGGER.debug("found " + ads.size() + " AdDefinitions");
			if (RuntimeContext.getAdDB().size() > 0) {
				RuntimeContext.getAdDB().clear();
			}
			
			for (AdDefinition ad : ads.values()) {

				// RuntimeContext.getAdDB().deleteBanner(ad.getId());
				RuntimeContext.getAdDB().addBanner(ad);
			}
			// reopen after all updates are done
			RuntimeContext.getAdDB().reopen();
		} catch (IOException e) {
			LOGGER.error("error running full update ", e);
		} finally {
			updateRunning = false;
			updateStarted = 0l;
		}
	}

	/**
	 * is currently a full update running
	 * 
	 * @return
	 */
	public boolean isUpdating() {
		return this.updateRunning;
	}
	
	/**
	 * 
	 * @return the timestamp when the update was startet
	 */
	public long getUpdateStarted() {
		return updateStarted;
	}


	class AdEntryListener implements EntryListener<String, AdDefinition> {

		private ClusterManager manager = null;

		public AdEntryListener(ClusterManager manager) {
			this.manager = manager;
		}

		@Override
		public void entryAdded(EntryEvent<String, AdDefinition> entry) {
			executor.submit(new AdTaskRunnable(new AdTask(entry.getValue(),
					entry.getKey(), false), this.manager));
		}

		@Override
		public void entryEvicted(EntryEvent<String, AdDefinition> entry) {
			executor.submit(new AdTaskRunnable(new AdTask(entry.getValue(),
					entry.getKey(), true), this.manager));
		}

		@Override
		public void entryRemoved(EntryEvent<String, AdDefinition> entry) {
			executor.submit(new AdTaskRunnable(new AdTask(entry.getValue(),
					entry.getKey(), true), this.manager));
		}

		@Override
		public void entryUpdated(EntryEvent<String, AdDefinition> entry) {
			executor.submit(new AdTaskRunnable(new AdTask(entry.getValue(),
					entry.getKey(), false), this.manager));
		}
	}

	static class AdTask {
		public final AdDefinition ad;
		public final String adid;
		public final boolean remove;

		public AdTask(AdDefinition ad, String adid, boolean remove) {
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
		private ClusterManager manager;

		public AdTaskRunnable(AdTask task, ClusterManager manager) {
			this.task = task;
			this.manager = manager;
		}

		@Override
		public void run() {
			if (RuntimeContext.getClusterManager().isUpdating()) {
				try {
					Thread.sleep(100);
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
				LOGGER.error(
						"error updating AdDefinition " + (task.toString()), e);
				// on error reindex the advertisements
				manager.init();
			}
		}

	}
}

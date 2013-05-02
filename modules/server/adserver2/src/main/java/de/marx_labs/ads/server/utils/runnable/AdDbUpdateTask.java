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
package de.marx_labs.ads.server.utils.runnable;

import java.util.TimerTask;

import de.marx_labs.ads.server.utils.RuntimeContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class AdDbUpdateTask extends TimerTask {

	private static final Logger logger = LoggerFactory.getLogger(AdDbUpdateTask.class);
	
	public static final long delay = 1000 * 60 * 5;
	public static final long period = 1000 * 60 * 5;
	
	private boolean running = false;
	
	@Override
	public void run() {
		
		if (running) {
			return;
		}
		
		try {
			running = true;
			logger.info("update banner database");
			
			if (RuntimeContext.getImporter() != null) {
				RuntimeContext.getImporter().runImport();
			}
			
			if (RuntimeContext.getAdDB() != null) {
				RuntimeContext.getAdDB().reopen();
			}
			
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			running = false;
		}
	}

}

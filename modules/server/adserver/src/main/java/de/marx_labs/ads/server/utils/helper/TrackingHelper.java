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
package de.marx_labs.ads.server.utils.helper;

import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.context.AdContext;
import de.marx_labs.ads.services.tracking.events.TrackEvent;
import net.minidev.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackingHelper {
	public static final Logger logger = LoggerFactory.getLogger(TrackingHelper.class);
	
	public static void trackEvent (AdContext context, TrackEvent event) {
		try {
			RuntimeContext.getTrackService().track(event);
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public static void trackImpression (AdContext context, TrackEvent event) {
		try {
			JSONObject json = new JSONObject();
			json.put("id", event.getId());
			json.put("bannerid", event.getBannerId());
			json.put("time", event.getTime());
			json.put("type", event.getType().getName());
			json.put("user", event.getUser());
			RuntimeContext.impressionLogger.impression(json.toJSONString());
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	public static void trackClick (AdContext context, TrackEvent event) {
		try {
			
			JSONObject json = new JSONObject();
			json.put("id", event.getId());
			json.put("bannerid", event.getBannerId());
			json.put("time", event.getTime());
			json.put("type", event.getType().getName());
			json.put("user", event.getUser());
			
			RuntimeContext.clickLogger.click(json.toJSONString());
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}

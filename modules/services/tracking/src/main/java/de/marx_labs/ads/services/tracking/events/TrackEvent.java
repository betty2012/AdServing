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
package de.marx_labs.ads.services.tracking.events;

import java.io.Serializable;
import java.util.EnumMap;

public class TrackEvent extends EnumMap<EventAttribute, String> implements Serializable{
	
	private long time;
	
	public TrackEvent() {
		super(EventAttribute.class);
	}
	
	
	public EventType getType () {
		if (containsKey(EventAttribute.TYPE)) {
			String type = get(EventAttribute.TYPE);
			
			return EventType.forName(type);
		}
		
		return EventType.ALL;
	}
	
	public String getSite () {
		return get(EventAttribute.SITE);
	}
	public void setSite (String site) {
		put(EventAttribute.SITE, site);
	}
	
	public String getCampaign () {
		return get(EventAttribute.CAMPAIGN);
	}
	public void setCampaign (String Campaign) {
		put(EventAttribute.CAMPAIGN, Campaign);
	}
	
	public String getId () {
		return get(EventAttribute.ID);
	}
	public void setId (String id) {
		put(EventAttribute.ID, id);
	}
	
	public String getIp () {
		return get(EventAttribute.IP);
	}
	public void setIp (String ip) {
		put(EventAttribute.IP, ip);
	}
	
	public String getUser () {
		return get(EventAttribute.USER);
	}
	public void setUser (String user) {
		put(EventAttribute.USER, user);
	}
	
	public String getBannerId () {
		return get(EventAttribute.AD_ID);
	}
	public void setBannerId (String bannerid) {
		put(EventAttribute.AD_ID, bannerid);
	}


	public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
	}
	
	
}

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
package de.marx_labs.ads.server.utils.context;

import net.sf.uadetector.UserAgent;
import de.marx_labs.ads.base.utils.BaseObject;
import de.marx_labs.ads.db.definition.AdSlot;

/**
 * Der AdContext enthält die wichtigsten Information zu einem Request.
 * Alle Informationen, die häufig benötigt werden sind hier enthalten
 * 
 * UserID 	= 	über die Benutzer-ID wird ein Benutzer markiert. Sie wird in einem 
 * 				Cookie gespeichert und ist 24 Stunden gültig.
 * 
 * RequestID = 	Die ID des Requests ist bei allen AdServer-Aufrufen, die von einer PageImpression
 * 				erzeugt werden identisch. Sie dient unter anderem dazu doppelte Einblendung zu vermeiden 
 * 				
 * ClientUUID = Die AdUUID enthält die Informationen über die Site, Zone und Places
 * 
 * @author thmarx
 *
 */
public class AdContext /*extends BaseObject*/ {
	
	private static final String USER_ID = "userid";
	private static final String REQUEST_ID = "requestid";
	private static final String SLOT = "slot";
	private static final String IP = "ip";
	private static final String USER_AGENT = "user_agent";
	
	private String clientIp;
	private String userId;
	private String requestId;
	private AdSlot adSlot;
	private UserAgent userAgent;
	
	public AdContext () {
		
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public AdSlot getAdSlot() {
		return adSlot;
	}

	public void setAdSlot(AdSlot adSlot) {
		this.adSlot = adSlot;
	}

	public UserAgent getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(UserAgent userAgent) {
		this.userAgent = userAgent;
	}
	
	
	
//	public UserAgent getUserAgent () {
//		return get(AdContext.USER_AGENT, UserAgent.class, null);
//	}
//	public void setUserAgent (UserAgent userAgent) {
//		put(AdContext.USER_AGENT, USER_AGENT);
//	}
//	
//	public String getIp () {
//		return get(AdContext.IP, String.class, null);
//	}
//	public void setIp (String ip) {
//		put(AdContext.IP, ip);
//	}
//	public String getUserid () {
//		return get(AdContext.USER_ID, String.class, null);
//	}
//	public void setUserid (String userid) {
//		put(AdContext.USER_ID, userid);
//	}
//	public String getRequestid () {
//		return get(AdContext.REQUEST_ID, String.class, null);
//	}
//	public void setRequestid (String requestid) {
//		put(AdContext.REQUEST_ID, requestid);
//	}
//	
//	public void setSlot (AdSlot slot) {
//		put(AdContext.SLOT, slot);
//	}
//	public AdSlot getSlot () {
//		return get(AdContext.SLOT, AdSlot.class, null);
//	}
}

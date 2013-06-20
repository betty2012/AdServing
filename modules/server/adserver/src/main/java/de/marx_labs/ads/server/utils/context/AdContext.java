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

import java.util.Locale;

import net.sf.uadetector.UserAgent;
import de.marx_labs.ads.db.definition.AdSlot;
import de.marx_labs.ads.services.geo.Location;

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
public class AdContext {
	
	private String clientIp;
	private String userId;
	private String requestId;
	private AdSlot adSlot;
	private UserAgent userAgent;
	private Locale locale;
	private Location location;
	
	public AdContext () {
		
	}

	
	
	
	
	public Location getLocation() {
		return location;
	}





	public void setLocation(Location location) {
		this.location = location;
	}





	public Locale getLocale() {
		return locale;
	}



	public void setLocale(Locale locale) {
		this.locale = locale;
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
}

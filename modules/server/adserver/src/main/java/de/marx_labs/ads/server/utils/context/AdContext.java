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
 * Der AdContext enthält die wichtigsten Information zu einem Request. Alle Informationen, die häufig benötigt werden
 * sind hier enthalten
 * 
 * UserID = über die Benutzer-ID wird ein Benutzer markiert. Sie wird in einem Cookie gespeichert und ist 24 Stunden
 * gültig.
 * 
 * RequestID = Die ID des Requests ist bei allen AdServer-Aufrufen, die von einer PageImpression erzeugt werden
 * identisch. Sie dient unter anderem dazu doppelte Einblendung zu vermeiden
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

	public AdContext() {

	}

	public Location location() {
		return location;
	}

	public AdContext location(Location location) {
		this.location = location;
		return this;
	}

	public Locale locale() {
		return locale;
	}

	public AdContext locale(Locale locale) {
		this.locale = locale;
		return this;
	}

	public String clientIP() {
		return clientIp;
	}

	public AdContext clientIP(String clientIp) {
		this.clientIp = clientIp;
		return this;
	}

	public String userID() {
		return userId;
	}

	public AdContext userID(String userId) {
		this.userId = userId;
		return this;
	}

	public String requestID() {
		return requestId;
	}

	public AdContext requestID(String requestId) {
		this.requestId = requestId;
		return this;
	}

	public AdSlot adSlot() {
		return adSlot;
	}

	public AdContext adSlot(AdSlot adSlot) {
		this.adSlot = adSlot;
		return this;
	}

	public UserAgent userAgent() {
		return userAgent;
	}

	public AdContext userAgent(UserAgent userAgent) {
		this.userAgent = userAgent;
		return this;
	}
}

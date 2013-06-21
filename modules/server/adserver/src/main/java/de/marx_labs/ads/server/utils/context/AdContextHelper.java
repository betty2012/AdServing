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


import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.uadetector.UserAgent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.marx_labs.ads.common.util.Strings;
import de.marx_labs.ads.db.definition.AdSlot;
import de.marx_labs.ads.server.utils.AdServerConstants;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.http.CookieUtils;
import de.marx_labs.ads.server.utils.http.RequestHelper;
import de.marx_labs.ads.services.geo.Location;


public class AdContextHelper {

	private static final Logger logger = LoggerFactory.getLogger(AdContextHelper.class);
	
	private static final CachedUserAgentStringParser userAgentParser = new CachedUserAgentStringParser();
	
	public static AdContext getAdContext (HttpServletRequest request, HttpServletResponse response) {
		AdContext context = new AdContext();
		
		String userID = null;
		Cookie cookie = CookieUtils.getCookie(request.getCookies(), AdServerConstants.Cookie.USERID);
		if (cookie != null) {
			userID = cookie.getValue();
		}
		if (Strings.isEmpty(userID)) {
			userID = UUID.randomUUID().toString();
			CookieUtils.addCookie(response, AdServerConstants.Cookie.USERID, userID, CookieUtils.ONE_YEAR, RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.COOKIE_DOMAIN));
		}
		context.setUserId(userID);
		
		String requestID = (String)request.getParameter(RequestHelper.requestId);
		if (Strings.isEmpty(requestID)) {
			requestID = UUID.randomUUID().toString();
		}
		context.setRequestId(requestID);
		
		String slot = (String)request.getParameter(RequestHelper.slot);
		if (!Strings.isEmpty(slot)) {
			try {
				AdSlot aduuid = AdSlot.fromString(slot);
				context.setAdSlot(aduuid);
			} catch (Exception e) {
				logger.error("", e);
			}
		}

		// gets the ip
		String clientIP = request.getRemoteAddr();
		
		/*
		 * if we are behind a proxy or loadbalancer
		 * the client ip is mostly set in one of the following header fields
		 */
		if (request.getHeader("X-Real-IP") != null) {
			clientIP = request.getHeader("X-Real-IP");
		} else if (request.getHeader("HTTP_X_FORWARDED_FOR") != null) {
			clientIP = request.getHeader("HTTP_X_FORWARDED_FOR");
		} else if (request.getHeader("X-Forwarded-For") != null) {
			clientIP = request.getHeader("X-Forwarded-For");
			
		}
		context.setClientIp(clientIP);
		
		UserAgent userAgent = userAgentParser.parse(request.getHeader("User-Agent"));
		context.setUserAgent(userAgent);
		
		context.setLocale(request.getLocale());
		
//		Location loc = RuntimeContext.getIpDB().searchIp(clientIP);
		try {
			Location loc = RuntimeContext.Caches.locations.get(clientIP);
			context.setLocation(loc);
		} catch (ExecutionException e) {
			logger.error("", e);
		}
		
		return context;
	}
}

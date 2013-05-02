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
package de.marx_labs.ads.server.utils.http;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import de.marx_labs.ads.common.util.Strings;



/**
 * Utilities zum arbeiten mit Cookies
 * 
 * @author thmarx
 */
public class CookieUtils {

	
	public static final int ONE_HOUR = 60 * 60;
	public static final int ONE_DAY = 60 * 60 * 24;
	public static final int ONE_WEEK = 60 * 60 * 24 * 7;
	public static final int ONE_MONTH = 60 * 60 * 24 * 30;
	public static final int ONE_YEAR = 12 * 60 * 60 * 24 * 30;

	/**
	 * Liefert ein Cookie mit einem bestimmte Namen aus einem Array von Cookies
	 * 
	 * @param cookies
	 *            ein Array von Cookies
	 * @param name
	 *            Der Name des Cookies
	 * @return das Cookie oder null, wenn keins mit diesem Namen gefunden wurdew
	 */
	public static Cookie getCookie(Cookie cookies[], String name) {

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}

		// we've got this far so the specified cookie wasn't found
		return null;
	}

	/**
	 * schreibt ein Cookie in den Request
	 * 
	 * @param response
	 *            der HttpServletResponse
	 * @param name
	 *            der Name des Cookie
	 * @param value
	 *            der Wert des Cookie
	 * @param maxAge
	 *            die Lebensdauer in Sekunden
	 */
	public static void addCookie(HttpServletResponse response, String name,
			String value, int maxAge, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		if (!Strings.isEmpty(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}

	/**
	 * entfernt ein cookie aus dem Request
	 * 
	 * @param response
	 *            der HttpServletResponse
	 * @param name
	 *            der Name des Cookies
	 */
	public static void removeCookie(HttpServletResponse response, String name) {
		addCookie(response, name, "", 0, null);
	}

}
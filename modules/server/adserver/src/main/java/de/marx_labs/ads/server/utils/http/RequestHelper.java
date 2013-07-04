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



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.marx_labs.ads.db.db.request.AdRequest;
import de.marx_labs.ads.db.enums.Day;
import de.marx_labs.ads.db.model.Country;
import de.marx_labs.ads.db.model.State;
import de.marx_labs.ads.db.services.AdFormats;
import de.marx_labs.ads.db.services.AdTypes;
import de.marx_labs.ads.db.utils.geo.GeoLocation;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.context.AdContext;
import de.marx_labs.ads.services.geo.Location;

/**
 * Helper zum erzeugen des AdRequest aus dem HttpRequest
 * 
 * @author tmarx
 * 
 */
public class RequestHelper {
	private static final Logger logger = LoggerFactory.getLogger(RequestHelper.class);

	public static final String format = "_p1";
	public static final String type = "_p2";
	public static final String offset = "_p3";
	public static final String requestId = "_p4";
	public static final String flash = "_p5";
	public static final String slot = "_p6";
	public static final String keywords = "_p7";
	public static final String referrer = "_p8";
	public static final String div_id = "_p9";
	public static final String product = "_p10";


	private static final Pattern integerPattern = Pattern.compile("^\\d+$");
	// prefix for custom key values
	private static final String KeyValuePrefix = "kv_";
	private static final int KeyValuePrefixLength = KeyValuePrefix.length();

	public static String[] getParameter (HttpServletRequest request, String name, String...defaultValue) {
		if (request.getParameterMap().containsKey(name)) {
			return request.getParameterMap().get(name);
		}
		
		return defaultValue;
	}
	
	/**
	 * extract the custom key values from the request
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getKeyValues (HttpServletRequest request) {
		Map<String, String> keywords = new HashMap<String, String>();
		
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameter = parameterNames.nextElement();
			
			if (parameter.startsWith(KeyValuePrefix)){
				String key = parameter.substring(KeyValuePrefixLength);
				String value = request.getParameter(parameter);
				keywords.put(key, value);
			}
		}
		
		return keywords;
	}
	
	public static AdRequest getAdRequest(AdContext context, HttpServletRequest request) {
		AdRequest adRequest = new AdRequest();

		// TODO: Uhrzeit auf länge und Integer prüfen
		// Matcher matchesInteger = integerPattern.matcher (temp);
		// boolean isInteger = matchesInteger.matches ();

		try {
			Location loc = context.location();
			if (loc != null) {
				try {
					GeoLocation geo = new GeoLocation(Double.parseDouble(loc.getLatitude()), Double.parseDouble(loc.getLongitude()));
					
					adRequest.geoLocation(geo);
					
					adRequest.country(new Country(loc.getCountry()));
					adRequest.state(new State(loc.getRegionName()));
				} catch (NumberFormatException nfe) {
//					logger.error("", nfe);
				}
			}

			// Format
			String format = request.getParameter(RequestHelper.format);
			// Type
			String type = (String) request.getParameter(RequestHelper.type);

			adRequest.formats().add(AdFormats.forCompoundName(format));
			adRequest.types().add(AdTypes.forType(type));
			
			adRequest.keywords(KeywordUtils.getKeywords(request));
			
			adRequest.locale(context.locale());
			
			adRequest.product(request.getParameter(RequestHelper.product));
			
			Map<String, String> keyValues = getKeyValues(request);
			adRequest.keyValues().putAll(keyValues);
			
			if (context.adSlot() != null) {
				adRequest.site(context.adSlot().getSite());
				adRequest.adSlot(context.adSlot().toString());
			}

			addTimeCondition(request, adRequest);
		} catch (Exception e) {
			logger.error("", e);
		}

		return adRequest;
	}

	/*
	 * Durch das übergebene Offset des Browsers können hier die Tageszeit und 
	 * das Datum gesetzt werden
	 */
	private static void addTimeCondition(HttpServletRequest request,
			AdRequest adRequest) {
		String strOffSet = request.getParameter(RequestHelper.offset);
		
		int offset = Integer.parseInt(strOffSet);
		offset = offset * 60000;
		String [] ids = TimeZone.getAvailableIDs(offset);
		
		Calendar temp = Calendar.getInstance();
		temp.setTimeZone(TimeZone.getTimeZone(ids[0]));
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		DateFormat timeFormat = new SimpleDateFormat("HHmm");
		
		// aktuelle Uhrzeit setzen
		adRequest.time(timeFormat.format(temp.getTime()));
		// aktuelles Datum setzen
		adRequest.date(dateFormat.format(temp.getTime()));
		// Tag der Woche setzten
		adRequest.day(Day.getDayForJava(temp.get(Calendar.DAY_OF_WEEK)));
	}
}

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
package de.marx_labs.ads.adlytics.servlets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import de.marx_labs.ads.adlytics.utils.RuntimeContext;
import de.marx_labs.ads.common.util.EncodeHelper;

public abstract class TrackingServlet extends HttpServlet {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrackingServlet.class);
	public static final String EMPTY_VALUE = "<empty value>";
	
	protected class JsonLogger implements Runnable {
		AsyncContext asyncContext;
		
		public JsonLogger(AsyncContext asyncContext) {
			this.asyncContext = asyncContext;
		}

		@Override
		public void run() {

			try {
				JSONObject json = new JSONObject();
				
				ServletRequest request = asyncContext.getRequest();
				Enumeration<String> names = request.getParameterNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					String []values = request.getParameterValues(name);
					
					JSONValue value = null;
					if (values.length > 1) {
						json.put(name, values);
					} else if (values.length == 1) {
						json.put(name, values[0]);
					} else {
						json.put(name, "<empty value>");
					}
				}

				
				
				DBObject dbObject = (DBObject) JSON.parse(json.toJSONString());

				if (dbObject.keySet().isEmpty()) {
					LOGGER.warn("logging of empty object stopped");
				}
				
				RuntimeContext.db().getCollection("tracking").insert(dbObject);
				
				String _date = EncodeHelper.decodeURIComponent(request.getParameter("_date"));
				Date d = parseGMT(_date);
				System.out.println(request.getParameter("_date") + " / " + _date + " = " + d);
			} catch (Exception e) {
				LOGGER.error("", e);
				throw new RuntimeException(e);
			} finally {
				asyncContext.complete();
			}
		}
	}
	
	protected static Date parseGMT (String visitorTime) {
		SimpleDateFormat FORMAT = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.UK);
//		Date D = FORMAT.parse("Sun, 25 Mar 2007 13:36:54 GMT");
//		System.out.println(D);
		try {
			return FORMAT.parse(visitorTime);
		} catch (ParseException e) {
			LOGGER.error("", e);
		}
		return null;
	}
}

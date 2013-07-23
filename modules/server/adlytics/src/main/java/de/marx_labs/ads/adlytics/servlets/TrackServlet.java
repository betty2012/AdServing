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

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import de.marx_labs.ads.adlytics.utils.RuntimeContext;

/**
 * @author Keesun Baik To test this class, turn off all filters in web.xml.
 */
@WebServlet(value = "/track", asyncSupported = true)
public class TrackServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		final AsyncContext asyncContext = request.startAsync(request, response);

		asyncContext.start(new JsonLogger(asyncContext));
	}

	private class JsonLogger implements Runnable {
		AsyncContext asyncContext;
		
		public JsonLogger(AsyncContext asyncContext) {
			this.asyncContext = asyncContext;
		}

		@Override
		public void run() {

			try {

				StringBuffer jb = new StringBuffer();
				String line = null;

				BufferedReader reader = asyncContext.getRequest().getReader();
				while ((line = reader.readLine()) != null) {
					jb.append(line);
				}

				// should be used for validation
				// JSONObject obj = (JSONObject) JSONValue.parse(json);

				DBObject dbObject = (DBObject) JSON.parse(jb.toString());

				RuntimeContext.db().getCollection("tracking").insert(dbObject);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				asyncContext.complete();
			}
		}
	}
}
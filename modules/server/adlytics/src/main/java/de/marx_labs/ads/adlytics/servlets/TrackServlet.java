package de.marx_labs.ads.adlytics.servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import de.marx_labs.ads.adlytics.utils.RuntimeContext;

/**
 * @author Keesun Baik To test this class, turn off all filters in web.xml.
 */
@WebServlet(value = "/track", asyncSupported = true)
public class TrackServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final AsyncContext asyncContext = request.startAsync(request, response);

		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
		} catch (Exception e) { /* report an error */
		}

		asyncContext.start(new JsonLogger(asyncContext, jb.toString()));
	}

	private class JsonLogger implements Runnable {
		AsyncContext asyncContext;
		String json;

		public JsonLogger(AsyncContext asyncContext, String json) {
			this.asyncContext = asyncContext;
			this.json = json;
		}

		@Override
		public void run() {

			try {
				// should be used for validation
				// JSONObject obj = (JSONObject) JSONValue.parse(json);
				
				DBObject dbObject = (DBObject) JSON.parse(json);
				
				RuntimeContext.db().getCollection("tracking").insert(dbObject);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				asyncContext.complete();
			}
		}
	}
}
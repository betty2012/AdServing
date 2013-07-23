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

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
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
public class ImageServlet extends HttpServlet {

	private byte[] image = null;
	
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		try {
			this.image = BufferedImageToByte(getImage());
		} catch (IOException e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		final AsyncContext asyncContext = request.startAsync(request, response);

//		StringBuffer jb = new StringBuffer();
//		String line = null;
//		try {
//			BufferedReader reader = request.getReader();
//			while ((line = reader.readLine()) != null) {
//				jb.append(line);
//			}
//		} catch (Exception e) { /* report an error */
//		}

//		asyncContext.start(new JsonLogger(asyncContext, jb.toString()));
		
//		executor.execute(new JsonLogger(jb.toString()));
		
		response.setHeader("Content-Type", "image/gif");
		response.setHeader("Content-Length", String.valueOf(image.length));
		response.setHeader("Content-Disposition", "inline; filename=\"1x1.gif\"");
		
		response.getOutputStream().write(image);
	}

	private class JsonLogger implements Runnable {
		String json;

		public JsonLogger(String json) {
		
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
				
			}
		}
	}

	private BufferedImage getImage() throws IOException {
		BufferedImage sourceImage = null;

		URL url = this.getClass().getResource("1x1.gif");
		return ImageIO.read(url);
	}
	
	public static byte[] BufferedImageToByte(BufferedImage bild) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bild, "gif", byteArrayOutputStream);
            byte[] imageData = byteArrayOutputStream.toByteArray();
            return imageData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
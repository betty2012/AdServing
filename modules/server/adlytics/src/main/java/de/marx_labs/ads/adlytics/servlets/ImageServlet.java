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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Keesun Baik To test this class, turn off all filters in web.xml.
 */
@WebServlet(value = "/track", asyncSupported = true)
public class ImageServlet extends TrackingServlet {

	private byte[] image = null;
	
//	private ExecutorService executor = Executors.newSingleThreadExecutor();

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
		
		response.setHeader("Content-Type", "image/gif");
		response.setHeader("Content-Length", String.valueOf(image.length));
		response.setHeader("Content-Disposition", "inline; filename=\"1x1.gif\"");
		
		response.getOutputStream().write(image);
		
		final AsyncContext asyncContext = request.startAsync(request, response);

		asyncContext.start(new JsonLogger(asyncContext));
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
/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
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
package net.mad.ads.server.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mad.ads.db.definition.AdDefinition;
import net.mad.ads.server.utils.RuntimeContext;
import net.mad.ads.server.utils.context.AdContext;
import net.mad.ads.server.utils.helper.TrackingHelper;
import net.mad.ads.server.utils.http.listener.AdContextListener;
import net.mad.ads.services.tracking.events.ClickTrackEvent;
import net.mad.ads.services.tracking.events.TrackEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet implementation class AdClick
 */
@WebServlet(asyncSupported = true)
public class AdClick extends HttpServlet {
	public static final int CALLBACK_TIMEOUT = 60000;

	private static final Logger logger = LoggerFactory.getLogger(AdClick.class);

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdClick() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		// create the async context, otherwise getAsyncContext() will be null
		final AsyncContext ctx = request.startAsync();

		// set the timeout
		ctx.setTimeout(CALLBACK_TIMEOUT);

		// attach listener to respond to lifecycle events of this AsyncContext
		ctx.addListener(new AsyncListener() {
			/**
			 * complete() has already been called on the async context, nothing
			 * to do
			 */
			public void onComplete(AsyncEvent event) throws IOException {
			}

			/** timeout has occured in async task... handle it */
			public void onTimeout(AsyncEvent event) throws IOException {
				log("onTimeout called");
				log(event.toString());
				ctx.getResponse().getWriter().write("TIMEOUT");
				ctx.complete();
			}

			/**
			 * THIS NEVER GETS CALLED - error has occured in async task...
			 * handle it
			 */
			public void onError(AsyncEvent event) throws IOException {
				log("onError called");
				log(event.toString());
				ctx.getResponse().getWriter().write("ERROR");
				ctx.complete();
			}

			/** async context has started, nothing to do */
			public void onStartAsync(AsyncEvent event) throws IOException {
			}
		});

		final AdContext context = AdContextListener.ADCONTEXT.get();
		ctx.start(new Runnable() {

			@Override
			public void run() {
				try {
					String id = (String) request.getParameter("id");

					AdDefinition banner = RuntimeContext.getAdDB()
							.getBanner(id);

					try {
						
						TrackEvent trackEvent = new ClickTrackEvent();
						trackEvent.setBannerId(banner.getId());
						trackEvent.setCampaign(banner.getCampaign() != null ? banner
								.getCampaign().getId() : "");
						trackEvent.setUser(context.getUserid());
						trackEvent.setId(UUID.randomUUID().toString());
						trackEvent.setTime(System.currentTimeMillis());
						trackEvent.setIp(context.getIp());
						
						if (context.getSlot() != null) {
							trackEvent.setSite(context.getSlot().getSite());
						} else {
							trackEvent.setSite("NONE_PAGE");
						}

						// RuntimeContext.getTrackService().track(trackEvent);
						// RuntimeContext.clickLogger.click(banner.getId(),
						// context.getUserid(), ""+trackEvent.getTime());
						TrackingHelper.trackEvent(context, trackEvent);
						TrackingHelper.trackClick(context, trackEvent);
					} catch (Exception e) {
						logger.error("", e);
					}

					response.sendRedirect(banner.getTargetUrl());
				} catch (IOException e) {
					logger.error("", e);
				} finally {
					ctx.complete();
				}
			}
		});

	}
}

/**
 * Mad-Advertisement
 * Copyright (C) 2011 Thorsten Marx <thmarx@gmx.net>
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
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
import net.mad.ads.server.utils.renderer.AdDefinitionRenderer;
import net.mad.ads.server.utils.renderer.AdDefinitionRendererService;
import net.mad.ads.server.utils.selection.AdProvider;
import net.mad.ads.services.tracking.events.ImpressionTrackEvent;
import net.mad.ads.services.tracking.events.TrackEvent;

/**
 * Servlet implementation class AdSelect
 */
@WebServlet(asyncSupported = true)
public class AdSelect extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final int CALLBACK_TIMEOUT = 60000;

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

	private void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {

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
				response.setContentType("text/javascript;charset=UTF-8");

				try {
					// Type
					// String type =
					// (String)request.getParameter(RequestHelper.type);
					// if (type == null || type.equals("")) {
					// type = "1";
					// }
					/*
					 * Aus den restlichen Bannern eins auswählen
					 * 
					 * Aktuell wird dies zufällig gemacht!
					 */
					AdDefinition banner = AdProvider.getInstance().getBanner(
							context, request);

					StringBuilder sb = new StringBuilder();
					if (banner != null) {
						/*
						 * Hier wird der Type des Banners verwendet und nicht
						 * der Typ der im Request Übergeben wird, da bei der
						 * Auswahl des Banners evtl. ein Fallback auf einen
						 * anderen BannerType erfolgen kann. z.B. Flashbanner
						 * auf Imagebanner
						 */
						// if
						// (banner.getType().getType().equals(ExternAdType.TYPE))
						// {
						// sb.append(ExternAdDefinitionRenderer.getInstance().render(banner,
						// request));
						// } else if
						// (banner.getType().getType().equals(ImageAdType.TYPE))
						// {
						// sb.append(ImageAdDefinitionRenderer.getInstance().render(banner,
						// request));
						// } else if
						// (banner.getType().getType().equals(FlashAdType.TYPE))
						// {
						// sb.append(FlashAdDefinitionRenderer.getInstance().render(banner,
						// request));
						// } else if
						// (banner.getType().getType().equals(ExpandableImageAdType.TYPE))
						// {
						// sb.append(ExpandableImageAdDefinitionRenderer.getInstance().render(banner,
						// request));
						// }
						AdDefinitionRenderer<AdDefinition> renderer = AdDefinitionRendererService
								.forType(banner.getType());
						sb.append(renderer.render(banner, request, context));

						TrackEvent trackEvent = new ImpressionTrackEvent();
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

						TrackingHelper.trackEvent(context, trackEvent);

						TrackingHelper.trackImpression(context, trackEvent);

						/*
						 * 
						 * Add request id and the ad id to the requested ad
						 * cache to rembemer which ad was displayed at the
						 * current pageview. This is used in the
						 * DuplicateBannerFilter to filter duplicate ads
						 * 
						 * Hier merken wir uns das Banner für diesen Request um
						 * später im DuplicateBannerFilter die Information
						 * verwenden zu können
						 * 
						 * Als Request gelten alle Aufrufe, die durch den selben
						 * Pageview erzeugt werden
						 * 
						 * pv = pageview (all request from a single pageview)
						 */
						RuntimeContext.getRequestBanners().put(
								"pv" + context.getRequestid() + "_"
										+ banner.getId(), Boolean.TRUE);
						/*
						 * Hier merken wir uns, dass ein Benutzer das Banner
						 * schon gesehen hat. Auf diese Art kann später z.B.
						 * geregelt werden, dass ein USER ein Banner maximal 5
						 * mal sehen soll
						 * 
						 * TODO: hier muss noch die TimeToLife für das
						 * Cacheobjekte gesetzt werden
						 * 
						 * u = user
						 */
						RuntimeContext.getRequestBanners().put(
								"u" + context.getUserid() + "_"
										+ banner.getId(), Boolean.TRUE);

						/*
						 * Damit wir später die passenden Banner für die
						 * Produkte anzeigen können, merken wir uns auch das
						 * Produkt
						 * 
						 * prod = product
						 */
						if (banner.isProduct()) {
							RuntimeContext
									.getRequestBanners()
									.put("prod" + context.getRequestid() + "_"
											+ banner.getProduct(), Boolean.TRUE);
						}
					}

					response.getWriter().write(sb.toString());

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					ctx.complete();
				}
			}

		});

	}
}

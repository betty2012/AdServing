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
package de.marx_labs.ads.server.servlet.async;

import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.marx_labs.ads.db.definition.AdDefinition;
import de.marx_labs.ads.server.servlet.AsyncAdServlet;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.context.AdContext;
import de.marx_labs.ads.server.utils.helper.TrackingHelper;
import de.marx_labs.ads.server.utils.renderer.AdDefinitionRenderer;
import de.marx_labs.ads.server.utils.renderer.AdDefinitionRendererService;
import de.marx_labs.ads.server.utils.renderer.AsyncAdDefinitionRenderer;
import de.marx_labs.ads.server.utils.renderer.AsyncAdDefinitionRendererService;
import de.marx_labs.ads.server.utils.selection.AdProvider;
import de.marx_labs.ads.services.tracking.events.ImpressionTrackEvent;
import de.marx_labs.ads.services.tracking.events.TrackEvent;

/**
 * Servlet implementation class AdSelect
 */
@WebServlet(asyncSupported = true)
public class AdSelect extends AsyncAdServlet {
	private static final long serialVersionUID = 1L;
	

	@Override
	protected void execute(final AsyncContext ctx, final AdContext context, final HttpServletRequest request,
			final HttpServletResponse response) {

		// exec.execute(new Runnable() {
		ctx.start(new Runnable() {
			@Override
			public void run() {
				
				PrintWriter out = null;
				
				response.setContentType("text/javascript;charset=UTF-8");

				try {
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
						AsyncAdDefinitionRenderer<AdDefinition> renderer = AsyncAdDefinitionRendererService
								.forType(banner.getType());
						sb.append(renderer.render(banner, request, context));

						TrackEvent trackEvent = new ImpressionTrackEvent();
						trackEvent.setBannerId(banner.getId());
						trackEvent.setCampaign(banner.getCampaign() != null ? banner
								.getCampaign().getId() : "");
						trackEvent.setUser(context.getUserId());
						trackEvent.setId(UUID.randomUUID().toString());
						trackEvent.setTime(System.currentTimeMillis());
						trackEvent.setIp(context.getClientIp());
						if (context.getAdSlot() != null) {
							trackEvent.setSite(context.getAdSlot().getSite());
						} else {
							trackEvent.setSite("NONE_PAGE");
						}

						TrackingHelper.trackEvent(context, trackEvent);

						TrackingHelper.trackImpression(context, trackEvent);

						/*
						 * 
						 * Add request id and the ad id to the requested ad
						 * cache to remember which ad was displayed at the
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
								"pv" + context.getRequestId() + "_"
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
								"u" + context.getUserId() + "_"
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
									.put("prod" + context.getRequestId() + "_"
											+ banner.getProduct(), Boolean.TRUE);
						}
					}
					
					response.setContentType("application/javascript");
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

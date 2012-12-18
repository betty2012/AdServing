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
package net.mad.ads.server.utils.renderer.impl;

import javax.servlet.http.HttpServletRequest;

import net.mad.ads.base.utils.render.RenderContext;
import net.mad.ads.db.definition.impl.ad.expandable.ExpandableImageAdDefinition;
import net.mad.ads.db.model.type.AdType;
import net.mad.ads.db.model.type.impl.ExpandableImageAdType;
import net.mad.ads.db.services.AdTypes;
import net.mad.ads.server.utils.AdServerConstants;
import net.mad.ads.server.utils.RuntimeContext;
import net.mad.ads.server.utils.context.AdContext;
import net.mad.ads.server.utils.renderer.AdDefinitionRenderer;
import net.mad.ads.server.utils.request.RequestHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Renderer für die Bannerdefinitionen des Types Image
 * 
 * @author tmarx
 * 
 */
public class ExpandableImageAdDefinitionRenderer implements
		AdDefinitionRenderer<ExpandableImageAdDefinition> {

	public static final Logger logger = LoggerFactory
			.getLogger(ExpandableImageAdDefinitionRenderer.class);

	// public static AdDefinitionRenderer<ExpandableImageAdDefinition> INSTANCE
	// = null;

	public ExpandableImageAdDefinitionRenderer() {
	}

	// public static synchronized AdDefinitionRenderer getInstance () {
	// if (INSTANCE == null) {
	// INSTANCE = new ExpandableImageAdDefinitionRenderer();
	// }
	//
	// return INSTANCE;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.mad.ads.server.utils.renderer.BannerDefinitionRenderer#render(net
	 * .mad.ads.api.definition.impl.image.ImageBannerDefinition)
	 */
	@Override
	public String render(ExpandableImageAdDefinition banner,
			HttpServletRequest request, AdContext context) {

		String clickurl = RuntimeContext.getProperties().getProperty(
				AdServerConstants.CONFIG.PROPERTIES.CLICK_URL);
		String staticurl = RuntimeContext.getProperties().getProperty(
				AdServerConstants.CONFIG.PROPERTIES.STATIC_URL);
		if (!staticurl.endsWith("/")) {
			staticurl += "/";
		}

		RenderContext renderContext = new RenderContext();
		renderContext.put("banner", banner);
		renderContext.put("staticUrl", staticurl);
		renderContext.put("clickUrl", clickurl + "?id=" + banner.getId() + "&" + RequestHelper.slot + "=" + context.getSlot().toString());

		if (request.getParameterMap().containsKey("tcolor")) {
			context.put("tcolor", request.getParameter("tcolor"));
		} else {
			context.put("tcolor", "#000000");
		}
		if (request.getParameterMap().containsKey("bcolor")) {
			context.put("bcolor", request.getParameter("bcolor"));
		} else {
			context.put("bcolor", "#cccccc");
		}

		try {
			return RuntimeContext.getBannerRenderer().render(
					AdTypes.forType(ExpandableImageAdType.TYPE).getName()
							.toLowerCase(), renderContext);
		} catch (Exception e) {
			logger.error("", e);
		}

		return "";
	}

	@Override
	public AdType getType() {
		return AdTypes.forType(ExpandableImageAdType.TYPE);
	}
}

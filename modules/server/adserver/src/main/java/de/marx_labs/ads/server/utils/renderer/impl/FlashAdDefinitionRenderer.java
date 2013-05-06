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
package de.marx_labs.ads.server.utils.renderer.impl;

import javax.servlet.http.HttpServletRequest;

import de.marx_labs.ads.base.utils.render.RenderContext;
import de.marx_labs.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.FlashAdType;
import de.marx_labs.ads.db.services.AdTypes;
import de.marx_labs.ads.server.utils.AdServerConstants;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.context.AdContext;
import de.marx_labs.ads.server.utils.renderer.AdDefinitionRenderer;
import de.marx_labs.ads.server.utils.request.RequestHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Renderer für die Bannerdefinitionen des Types Image
 * 
 * @author tmarx
 *
 */
public class FlashAdDefinitionRenderer implements AdDefinitionRenderer<FlashAdDefinition> {
	
	public static final Logger logger = LoggerFactory.getLogger(FlashAdDefinitionRenderer.class);
	
//	public static AdDefinitionRenderer<FlashAdDefinition> INSTANCE = null;
	
	public FlashAdDefinitionRenderer () {
	}
	
//	public static synchronized AdDefinitionRenderer getInstance () {
//		if (INSTANCE == null) {
//			INSTANCE = new FlashAdDefinitionRenderer();
//		}
//		
//		return INSTANCE;
//	}
	
	/* (non-Javadoc)
	 * @see de.marx_labs.ads.server.utils.renderer.BannerDefinitionRenderer#render(de.marx_labs.ads.api.definition.impl.image.ImageBannerDefinition)
	 */
	@Override
	public String render (FlashAdDefinition banner, HttpServletRequest request, AdContext context) {
		
		String clickurl = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.CLICK_URL);
		String staticurl = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.STATIC_URL);
		if (!staticurl.endsWith("/")) {
			staticurl += "/";
		}
		
		RenderContext renderContext = new RenderContext();
		renderContext.put("banner", banner);
		renderContext.put("staticUrl", staticurl);
		
		String toClickUrl = clickurl + "?id=" + banner.getId();
		if (context.getAdSlot() != null) {
			toClickUrl += "&" + RequestHelper.slot + "=" + context.getAdSlot().toString();
		}
		renderContext.put("clickUrl", toClickUrl);
		
		try {
			return RuntimeContext.getBannerRenderer().render(AdTypes.forType(FlashAdType.TYPE).getName().toLowerCase(), renderContext);
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return "";
	}
	
	@Override
	public AdType getType() {
		return AdTypes.forType(FlashAdType.TYPE);
	}
}

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
package de.marx_labs.ads.server.utils.renderer.impl.async;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.marx_labs.ads.base.utils.render.RenderContext;
import de.marx_labs.ads.db.definition.impl.ad.image.ImageAdDefinition;
import de.marx_labs.ads.db.model.type.AdType;
import de.marx_labs.ads.db.model.type.impl.ImageAdType;
import de.marx_labs.ads.db.services.AdTypes;
import de.marx_labs.ads.server.utils.AdServerConstants;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.context.AdContext;
import de.marx_labs.ads.server.utils.renderer.AsyncAdDefinitionRenderer;
import de.marx_labs.ads.server.utils.request.RequestHelper;

/**
 * Renderer für die Bannerdefinitionen des Types Image
 * 
 * @author tmarx
 *
 */
public class AsyncImageAdDefinitionRenderer implements AsyncAdDefinitionRenderer<ImageAdDefinition> {
	
	public static final Logger logger = LoggerFactory.getLogger(AsyncImageAdDefinitionRenderer.class);
	
	public AsyncImageAdDefinitionRenderer () {
	}
	
	/* (non-Javadoc)
	 * @see de.marx_labs.ads.server.utils.renderer.BannerDefinitionRenderer#render(de.marx_labs.ads.api.definition.impl.image.ImageBannerDefinition)
	 */
	@Override
	public String render (ImageAdDefinition banner, HttpServletRequest request, AdContext context) {
		
		String clickurl = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.CLICK_URL);
		String staticurl = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.STATIC_URL);
		if (!Strings.isNullOrEmpty(staticurl) && !staticurl.endsWith("/")) {
			staticurl += "/";
		}
		
		RenderContext renderContext = new RenderContext();
		renderContext.put("banner", banner);
		renderContext.put("staticUrl", staticurl);
		renderContext.put("divId", RequestHelper.getParameter(request, RequestHelper.div_id, "")[0]);
		
		String toClickUrl = clickurl + "?id=" + banner.getId();
		if (context.getAdSlot() != null) {
			toClickUrl += "&" + RequestHelper.slot + "=" + context.getAdSlot().toString();
		}
		renderContext.put("clickUrl", toClickUrl);
		
		try {
			return RuntimeContext.getBannerRenderer().render(AdTypes.forType(ImageAdType.TYPE).getName().toLowerCase() + "_async", renderContext);
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return "";
	}
	
	@Override
	public AdType getType() {
		return AdTypes.forType(ImageAdType.TYPE);
	}
}

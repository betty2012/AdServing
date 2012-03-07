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
package net.mad.ads.server.utils.renderer.impl;

import javax.servlet.http.HttpServletRequest;

import net.mad.ads.base.api.render.RenderContext;
import net.mad.ads.db.definition.impl.ad.flash.FlashAdDefinition;
import net.mad.ads.db.model.type.impl.FlashAdType;
import net.mad.ads.db.services.AdTypes;
import net.mad.ads.server.utils.AdServerConstants;
import net.mad.ads.server.utils.RuntimeContext;
import net.mad.ads.server.utils.renderer.AdDefinitionRenderer;

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
	
	public static AdDefinitionRenderer<FlashAdDefinition> INSTANCE = null;
	
	private FlashAdDefinitionRenderer () {
	}
	
	public static synchronized AdDefinitionRenderer getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new FlashAdDefinitionRenderer();
		}
		
		return INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see net.mad.ads.server.utils.renderer.BannerDefinitionRenderer#render(net.mad.ads.api.definition.impl.image.ImageBannerDefinition)
	 */
	@Override
	public String render (FlashAdDefinition banner, HttpServletRequest request) {
		
		String clickurl = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.CLICK_URL);
		String staticurl = RuntimeContext.getProperties().getProperty(AdServerConstants.CONFIG.PROPERTIES.STATIC_URL);
		if (!staticurl.endsWith("/")) {
			staticurl += "/";
		}
		
		RenderContext context = new RenderContext();
		context.put("banner", banner);
		context.put("staticUrl", staticurl);
		context.put("clickUrl", clickurl + "?id=" + banner.getId());
		
		try {
			return RuntimeContext.getBannerRenderer().render(AdTypes.forType(FlashAdType.TYPE).getName().toLowerCase(), context);
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return "";
	}
}

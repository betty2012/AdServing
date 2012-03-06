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
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.enums.AdType;
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
public class ImageAdDefinitionRenderer implements AdDefinitionRenderer<ImageAdDefinition> {
	
	public static final Logger logger = LoggerFactory.getLogger(ImageAdDefinitionRenderer.class);
	
	public static AdDefinitionRenderer<ImageAdDefinition> INSTANCE = null;
	
	private ImageAdDefinitionRenderer () {
	}
	
	public static synchronized AdDefinitionRenderer getInstance () {
		if (INSTANCE == null) {
			INSTANCE = new ImageAdDefinitionRenderer();
		}
		
		return INSTANCE;
	}
	
	/* (non-Javadoc)
	 * @see net.mad.ads.server.utils.renderer.BannerDefinitionRenderer#render(net.mad.ads.api.definition.impl.image.ImageBannerDefinition)
	 */
	@Override
	public String render (ImageAdDefinition banner, HttpServletRequest request) {
		
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
			return RuntimeContext.getBannerRenderer().render(AdType.IMAGE.getName().toLowerCase(), context);
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return "";
	}
}

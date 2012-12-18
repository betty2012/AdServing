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
package net.mad.ads.base.utils.render;

import net.mad.ads.base.utils.render.impl.freemarker.FreemarkerAdRenderer;
import net.mad.ads.db.definition.impl.ad.image.ImageAdDefinition;
import net.mad.ads.db.model.format.impl.HalfBannerAdFormat;

public class ImageAdRenderTest {
	public static void main (String [] args) throws Exception {
		
		ImageAdDefinition banner = new ImageAdDefinition();
		banner.setFormat(new HalfBannerAdFormat());
		banner.setImageUrl("test.jpg");
		
		RenderContext context = new RenderContext();
		context.put("banner", banner);
		
		
		AdRenderer tm = new FreemarkerAdRenderer();
		tm.init("testdata/templates/banner");
		tm.registerTemplate("image", "image.ftl");
		
		System.out.println(tm.render("image", context));
	}
}

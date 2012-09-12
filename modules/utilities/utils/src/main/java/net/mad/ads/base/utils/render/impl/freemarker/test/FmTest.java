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
package net.mad.ads.base.utils.render.impl.freemarker.test;


import java.util.HashMap;
import java.util.Map;

import net.mad.ads.base.utils.render.AdRenderer;
import net.mad.ads.base.utils.render.RenderContext;
import net.mad.ads.base.utils.render.impl.freemarker.FreemarkerAdRenderer;


public class FmTest {
	public static void main (String [] args) throws Exception {
		
		
		RenderContext context = new RenderContext();
		context.put("server", "www.myserver1.de");
		
		
		AdRenderer tm = new FreemarkerAdRenderer();
		tm.init("testdata/templates");
		tm.registerTemplate("test", "test.ftl");
		
		System.out.println(tm.render("test", context));
	}
}

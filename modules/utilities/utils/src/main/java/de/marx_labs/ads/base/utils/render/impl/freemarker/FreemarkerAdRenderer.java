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
package de.marx_labs.ads.base.utils.render.impl.freemarker;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import de.marx_labs.ads.base.utils.render.AdRenderer;
import de.marx_labs.ads.base.utils.render.RenderContext;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreemarkerAdRenderer implements AdRenderer {

	private Configuration cfg = null;
	
	private Map<String, Template> templates = null;
	
	@Override
	public void init(String templatePath) throws IOException {
		cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(templatePath));
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		
		this.templates = new HashMap<String, Template>();
	}

	@Override
	public void registerTemplate(String name, String file) throws IOException {
		Template temp = cfg.getTemplate(file);
		templates.put(name, temp);
	}

	@Override
	public String render(String name, RenderContext context) throws IOException {
		try {
			Template temp = templates.get(name);
			
			context.put("standardCompress", new freemarker.template.utility.StandardCompress());
			
			StringWriter sw = new StringWriter();
			BufferedWriter bw = new BufferedWriter(sw);
			temp.process(context, bw);
			bw.flush();
			return sw.toString();
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}

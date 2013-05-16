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
package de.marx_labs.ads.common.template.impl.velocity;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import de.marx_labs.ads.common.template.TemplateManager;



public class VMTemplateManager implements TemplateManager {

	private VelocityEngine ve = null;
	
	private Map<String, Template> templates = null;
	
	public void init(String templatePath) throws IOException {
		ve = new VelocityEngine();
		ve.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templatePath);
		try {
			ve.init();
		} catch (Exception e) {
			throw new IOException(e);
		}
		templates = new HashMap<String, Template>();
	}

	public void registerTemplate(String name, String file) throws IOException {
		try {
			Template t = ve.getTemplate(file);
			
			templates.put(name, t);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public String processTemplate(String name, Map<String, Object> parameters)
			throws IOException {
		
		try {
			VelocityContext context = new VelocityContext();
			if (parameters != null) {
				for (String key : parameters.keySet()) {
					context.put(key, parameters.get(key));
				}
			}
			
			Template t = templates.get(name);
			
			StringWriter sw = new StringWriter();
			BufferedWriter bw = new BufferedWriter(sw);
			
			t.merge(context, bw);
			bw.flush();
			return sw.toString();
		} catch (Exception e) {
			throw new IOException(e);
		}

	}

}

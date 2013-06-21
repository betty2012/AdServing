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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.marx_labs.ads.server.servlet.AsyncAdServlet;
import de.marx_labs.ads.server.utils.AdServerConstants;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.context.AdContext;
import de.marx_labs.ads.server.utils.http.RequestHelper;

/**
 * 
 * @author tmarx
 */
@WebServlet(asyncSupported = true)
public class AdServlet extends AsyncAdServlet {

	@Override
	protected void execute(final AsyncContext ctx, final AdContext adcontext, final HttpServletRequest request,
			final HttpServletResponse response) {

		// exec.execute(new Runnable() {
		ctx.start(new Runnable() {
			public void run() {
				PrintWriter out = null;
				try {
					out = response.getWriter();
					Map<String, Object> context = new HashMap<String, Object>();

					String adselect_url = RuntimeContext
							.getProperties()
							.getProperty(
									AdServerConstants.CONFIG.PROPERTIES.ADSERVER_ASYNC_SELECT_URL,
									"");
					String adserver_url = RuntimeContext
							.getProperties()
							.getProperty(
									AdServerConstants.CONFIG.PROPERTIES.ADSERVER_URL,
									"");
					context.put("adselect_url", adselect_url);
					context.put("adserver_url", adserver_url);
					
					context.put("ad_div_id", RequestHelper.getParameter(request, RequestHelper.div_id, "")[0]);
					context.put("ad_type", RequestHelper.getParameter(request, RequestHelper.type, "")[0]);
					context.put("ad_format", RequestHelper.getParameter(request, RequestHelper.format, "")[0]);
					
					context.put("adrequest_id", adcontext.getRequestId());
					context.put("enviroment", RuntimeContext.getEnviroment()
							.toLowerCase());

					String content = RuntimeContext.getTemplateManager()
							.processTemplate("ads_async", context);
					
					response.setContentType("application/javascript");
					out.println(content);
				} catch (Exception e) {
					log("Problem processing task", e);
					e.printStackTrace();
				} finally {
					if (out != null) {
						out.close();
					}
					ctx.complete();
				}

			}
		});
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "AdServlet for Async usage";
	}

}

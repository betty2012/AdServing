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
package de.marx_labs.ads.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.marx_labs.ads.server.utils.AdServerConstants;
import de.marx_labs.ads.server.utils.RuntimeContext;
import de.marx_labs.ads.server.utils.context.AdContext;
import de.marx_labs.ads.server.utils.http.listener.AdContextListener;

/**
 * 
 * @author tmarx
 */
@WebServlet(asyncSupported = true)
public class AdServlet extends HttpServlet {

	public static final int CALLBACK_TIMEOUT = 60000;
	public static final int MAX_SIMULATED_TASK_LENGTH_MS = 5000;

	/** executor svc */
	private ExecutorService exec;

	/** create the executor */
	public void init() throws ServletException {

		int size = Integer.parseInt(getInitParameter("threadpoolsize"));
		exec = Executors.newFixedThreadPool(size);

	}

	/** destroy the executor */
	public void destroy() {

		exec.shutdown();

	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// if we run in clustermode and the db update is runnging
		if (RuntimeContext.getClusterManager() != null && RuntimeContext.getClusterManager().isUpdating()) {
			// return 404 to the loadbalancer (eq haproxy)
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		
		// create the async context, otherwise getAsyncContext() will be null
		final AsyncContext ctx = request.startAsync();

		// set the timeout
		ctx.setTimeout(CALLBACK_TIMEOUT);

		// attach listener to respond to lifecycle events of this AsyncContext
		ctx.addListener(new AsyncListener() {
			/**
			 * complete() has already been called on the async context, nothing
			 * to do
			 */
			public void onComplete(AsyncEvent event) throws IOException {
			}

			/** timeout has occured in async task... handle it */
			public void onTimeout(AsyncEvent event) throws IOException {
				log("onTimeout called");
				log(event.toString());
				ctx.getResponse().getWriter().write("TIMEOUT");
				ctx.complete();
			}

			/**
			 * THIS NEVER GETS CALLED - error has occured in async task...
			 * handle it
			 */
			public void onError(AsyncEvent event) throws IOException {
				log("onError called");
				log(event.toString());
				ctx.getResponse().getWriter().write("ERROR");
				ctx.complete();
			}

			/** async context has started, nothing to do */
			public void onStartAsync(AsyncEvent event) throws IOException {
			}
		});

		execute(ctx, AdContextListener.ADCONTEXT.get());

	}

	private void execute(final AsyncContext ctx, final AdContext adcontext) {

		// exec.execute(new Runnable() {
		ctx.start(new Runnable() {
			public void run() {
				ServletResponse response = ctx.getResponse();
				PrintWriter out = null;
				try {
					out = response.getWriter();
					Map<String, Object> context = new HashMap<String, Object>();

					String adselect_url = RuntimeContext
							.getProperties()
							.getProperty(
									AdServerConstants.CONFIG.PROPERTIES.ADSERVER_SELECT_URL,
									"");
					String adserver_url = RuntimeContext
							.getProperties()
							.getProperty(
									AdServerConstants.CONFIG.PROPERTIES.ADSERVER_URL,
									"");
					context.put("adselect_url", adselect_url);
					context.put("adserver_url", adserver_url);
					// context.put("adrequest_id",
					// AdContextListener.ADCONTEXT.get()
					// .getRequestid());
					context.put("adrequest_id", adcontext.getRequestId());
					context.put("enviroment", RuntimeContext.getEnviroment()
							.toLowerCase());

					String content = RuntimeContext.getTemplateManager()
							.processTemplate("ads", context);
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
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}

}

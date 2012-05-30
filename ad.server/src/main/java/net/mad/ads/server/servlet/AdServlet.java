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
package net.mad.ads.server.servlet;

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

import net.mad.ads.server.utils.AdServerConstants;
import net.mad.ads.server.utils.RuntimeContext;
import net.mad.ads.server.utils.http.listener.AdContextListener;

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
		
		// create the async context, otherwise getAsyncContext() will be null
		  final AsyncContext ctx = request.startAsync();

		  // set the timeout
		  ctx.setTimeout(CALLBACK_TIMEOUT);

		  // attach listener to respond to lifecycle events of this AsyncContext
		  ctx.addListener(new AsyncListener() {
		    /** complete() has already been called on the async context, nothing to do */
		    public void onComplete(AsyncEvent event) throws IOException { }
		    /** timeout has occured in async task... handle it */
		    public void onTimeout(AsyncEvent event) throws IOException {
		      log("onTimeout called");
		      log(event.toString());
		      ctx.getResponse().getWriter().write("TIMEOUT");
		      ctx.complete();
		    }
		    /** THIS NEVER GETS CALLED - error has occured in async task... handle it */
		    public void onError(AsyncEvent event) throws IOException {
		      log("onError called");
		      log(event.toString());
		      ctx.getResponse().getWriter().write("ERROR");
		      ctx.complete();
		    }
		    /** async context has started, nothing to do */
		    public void onStartAsync(AsyncEvent event) throws IOException { }
		  });
		
	}

	private void execute(final AsyncContext ctx) {

		exec.execute(new Runnable() {
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
					String adserver_url = RuntimeContext.getProperties().getProperty(
							AdServerConstants.CONFIG.PROPERTIES.ADSERVER_URL, "");
					context.put("adselect_url", adselect_url);
					context.put("adserver_url", adserver_url);
					context.put("adrequest_id", AdContextListener.ADCONTEXT.get()
							.getRequestid());

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

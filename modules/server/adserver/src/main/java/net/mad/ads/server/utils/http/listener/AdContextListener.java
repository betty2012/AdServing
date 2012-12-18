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
package net.mad.ads.server.utils.http.listener;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.mad.ads.server.utils.context.AdContext;
import net.mad.ads.server.utils.context.AdContextHelper;

/**
 * Der AdContextLister stellt den späteren Servlets den AdContext
 * über ein ThreadLocal Variable zur Verfügung
 * 
 * @author tmarx
 *
 */
public class AdContextListener implements Filter {

	public final static ThreadLocal<AdContext> ADCONTEXT = new ThreadLocal<AdContext>();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		
		AdContext context = AdContextHelper.getAdContext(req, res);
		ADCONTEXT.set(context);
		
		chain.doFilter(request, response);
		
		ADCONTEXT.remove();
	}

	@Override
	public void init(FilterConfig config) throws ServletException {}
	@Override
	public void destroy() {}

}

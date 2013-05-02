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
package de.marx_labs.ads.server.mocks;


import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;




public class MockServletContext implements ServletContext {

	private Map<String,String> initParameters = new HashMap<String, String>();
	private Map<String,Object> attributes = new HashMap<String, Object>();
	
	
	public String getContextPath() {
		return null;
	}

	@Override
	public ServletContext getContext(String uripath) {
		return null;
	}

	@Override
	public int getMajorVersion() {
		return 2;
	}

	@Override
	public int getMinorVersion() {
		return 5;
	}

	@Override
	public String getMimeType(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getResourcePaths(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Servlet getServlet(String name) throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getServlets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getServletNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void log(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log(Exception exception, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log(String message, Throwable throwable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRealPath(String path) {
		File base = new File(".");
		String realPath = base.getAbsolutePath();
		
		if (!realPath.endsWith("/")) {
			realPath += "/";
		}
		realPath += "src/main/webapp";
		
		return realPath;
	}

	@Override
	public String getServerInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInitParameter(String name) {
		return initParameters.get(name);
	}
	public Map<String, String> getInitParameters () {
		return initParameters;
	}

	@Override
	public Enumeration getInitParameterNames() {
		return null;
	}

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return attributes.get(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String name, Object object) {
		attributes.put(name, object);
	}

	@Override
	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	@Override
	public String getServletContextName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEffectiveMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEffectiveMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean setInitParameter(String name, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Dynamic addServlet(String servletName, String className) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dynamic addServlet(String servletName, Servlet servlet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dynamic addServlet(String servletName,
			Class<? extends Servlet> servletClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> clazz)
			throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
			String filterName, String className) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
			String filterName, Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(
			String filterName, Class<? extends Filter> filterClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> clazz)
			throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilterRegistration getFilterRegistration(String filterName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSessionTrackingModes(
			Set<SessionTrackingMode> sessionTrackingModes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(String className) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends EventListener> void addListener(T t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(Class<? extends EventListener> listenerClass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> clazz)
			throws ServletException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void declareRoles(String... roleNames) {
		// TODO Auto-generated method stub
		
	}

	

	
	

}
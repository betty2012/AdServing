package de.marx_labs.ads.server.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Configurable
public class TestServlet extends HttpServlet {

	
	@Autowired
	private TestService service;
	
	@Override
	public void init() throws ServletException {
	   super.init();
	   SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		service.print("hallo Test");
	}
}

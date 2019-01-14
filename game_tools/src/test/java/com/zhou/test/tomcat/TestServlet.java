package com.zhou.test.tomcat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhou.core.http.server.servlet.AbstractServlet;

public class TestServlet extends AbstractServlet {

	@Override
	protected void post(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.getWriter().println("TestServlet post...");
		resp.getWriter().flush();
	}

	@Override
	protected void get(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		resp.getWriter().println("TestServlet get...");
		resp.getWriter().flush();
	}

	@Override
	public String getHttpServletName() {
		return "test";
	}

	@Override
	public String getPattern() {
		return "/test";
	}

}

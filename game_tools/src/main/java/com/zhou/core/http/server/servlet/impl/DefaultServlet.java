package com.zhou.core.http.server.servlet.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhou.core.http.server.servlet.AbstractServlet;
import com.zhou.util.CalendarUtil;
	/**
	 * ƒ¨»œ π”√
	 * @author zhouyongjun
	 *
	 */
public class DefaultServlet extends AbstractServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getHttpServletName() {
		return "default";
	}

	@Override
	public String getPattern() {
		return "/default";
	}

	@Override
	protected void post(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		resp.getWriter().println(this.getClass().getName() +" do method["+req.getMethod()+"].");
		System.out.println(CalendarUtil.format(System.currentTimeMillis())+":"
				+CalendarUtil.format(req.getSession().getLastAccessedTime())+":"+req.getSession().getId());
		try {
			req.getRequestDispatcher("/index.jsp").forward(req, resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void get(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		post(req, resp);
	}

}

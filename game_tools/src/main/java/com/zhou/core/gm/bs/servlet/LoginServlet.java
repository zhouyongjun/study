package com.zhou.core.gm.bs.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhou.core.http.server.servlet.AbstractServlet;
import com.zhou.entitiy.ConsoleUser;

public class LoginServlet extends AbstractServlet {

	@Override
	protected void post(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String name = req.getParameter("user");
		String password = req.getParameter("password");
		String ip = req.getRemoteHost();
		ConsoleUser consoleUser = (ConsoleUser) req.getSession().getAttribute("loginUser");
		if (consoleUser == null)
		{
			consoleUser = new ConsoleUser();
			consoleUser.setName(name);
			consoleUser.setPassword(password);
			consoleUser.setIp(ip);
			consoleUser.setLoginTime(System.currentTimeMillis());
			req.getSession().setAttribute("loginUser", consoleUser);
		}
		/**
		 *     1、转发是在服务器端完成的，重定向是在客户端发生的；
		       2、转发的速度快，重定向速度慢；
		       3、转发是同一次请求，重定向是两次请求；
		       4、转发地址栏没有变化，重定向地址栏有变化；
		       5、转发必须是在同一台服务器下完成，重定向可以在不同的服务器下完成
		 */
		//重定向
//		resp.sendRedirect(req.getContextPath() + "/control.jsp");
		//请求转发：
//		req.getRequestDispatcher(req.getContextPath() + "/control/newserver.jsp").forward(req, resp);
		req.getRequestDispatcher("/selected/index.html").forward(req, resp);
		
	}

	@Override
	protected void get(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

	}

	@Override
	public String getHttpServletName() {
		return "login";
	}

	@Override
	public String getPattern() {
		return "/login";
	}

}

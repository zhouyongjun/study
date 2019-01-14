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
		 *     1��ת�����ڷ���������ɵģ��ض������ڿͻ��˷����ģ�
		       2��ת�����ٶȿ죬�ض����ٶ�����
		       3��ת����ͬһ�������ض�������������
		       4��ת����ַ��û�б仯���ض����ַ���б仯��
		       5��ת����������ͬһ̨����������ɣ��ض�������ڲ�ͬ�ķ����������
		 */
		//�ض���
//		resp.sendRedirect(req.getContextPath() + "/control.jsp");
		//����ת����
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

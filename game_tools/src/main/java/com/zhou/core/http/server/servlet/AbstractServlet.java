package com.zhou.core.http.server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
	 * 抽象的servlet
	 * @author zhouyongjun
	 *
	 */
public abstract class AbstractServlet extends HttpServlet {
	private static final Logger logger = LogManager.getLogger();
	private static final long serialVersionUID = 1L;

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
	{
		try {
			logger.debug("http :"+request.getRemoteAddr()+"  session["+request.getSession().getId()+"] doGet .getContextPath():"+request.getContextPath());
			get(request, response);
		} catch (Exception e) {
			logger.error("http :"+request.getRemoteAddr()+"  session["+request.getSession().getId()+"] doGet is error.",e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try {
			logger.debug("http :"+request.getRemoteAddr()+"  session["+request.getSession().getId()+"] doPost .getContextPath():"+request.getContextPath());
			System.out.println(request.getContextPath());
			post(request, response);
		} catch (Exception e) {
			logger.error("http :"+request.getRemoteAddr()+"  session["+request.getSession().getId()+"] doPost is error.",e);
		}
	}
	/**
	 * post 方法处理
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	protected abstract void post(HttpServletRequest req, HttpServletResponse resp) throws Exception ;
	/**
	 * get方法处理
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	protected abstract void get(HttpServletRequest req, HttpServletResponse resp) throws Exception ;

		/**
		 * http servlet name 设置
		 * 名字识别，servlet 和 server mapping配置标识
		 * 保证唯一性
		 * @return
		 */
	public abstract String getHttpServletName();
	/**
	 * tomcat context path后的web路径设置
	 * 保证唯一性
	 * @return
	 */
	public abstract String getPattern();	
}

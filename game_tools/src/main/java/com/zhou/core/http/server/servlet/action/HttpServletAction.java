package com.zhou.core.http.server.servlet.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpServletAction {
	/**
	 * post 方法处理
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception ;
	/**
	 * get方法处理
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	/**
	 *@description 行为类型名 
	 * @return 类型
	 */
	String getActionName();
}

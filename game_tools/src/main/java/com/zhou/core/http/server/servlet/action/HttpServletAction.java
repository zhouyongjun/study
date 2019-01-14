package com.zhou.core.http.server.servlet.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpServletAction {
	/**
	 * post ��������
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception ;
	/**
	 * get��������
	 * @param req
	 * @param resp
	 * @throws Exception
	 */
	void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	/**
	 *@description ��Ϊ������ 
	 * @return ����
	 */
	String getActionName();
}

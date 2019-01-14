package com.zhou.core.http.server.servlet.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.http.server.servlet.AbstractServlet;
/**
 * 颗粒化HttpServlet类，实现根据action 对应一个行为操作
 * @author zhouyongjun
 *
 */
public abstract class ActionServlet extends AbstractServlet{
	public static final String PARAMTER_KEY = "action";
	private static final Logger logger = LogManager.getLogger();
	private static final long serialVersionUID = 1L;

	//关联的action Class 集合
	protected Map<String,HttpServletAction> actions = new HashMap<>();
	
	public Map<String, HttpServletAction> getActions() {
		return actions;
	}

	public void setActions(Map<String, HttpServletAction> actions) {
		this.actions = actions;
		
	}

	@Override
	protected void get(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException
	{
		try {
			logger.debug("http :"+req.getRemoteAddr()+"  session["+req.getSession().getId()+"] doGet .getContextPath():"+req.getContextPath());
			String actionName = req.getParameter(PARAMTER_KEY);
			HttpServletAction actionInstance = getAction(actionName);
			if (actionInstance == null)
			{
				writeResponse(resp,"error : use action="+actionName+" is not exist.");
				return;
			}
			actionInstance.doGet(req, resp);
		} catch (Exception e) {
			logger.error("http :"+req.getRemoteAddr()+"  session["+req.getSession().getId()+"] doGet is error.",e);
		}
	}
	
	private void writeResponse(HttpServletResponse resp, String result) throws IOException {
		resp.getWriter().println(result);
		resp.getWriter().flush();
	}
	
	/**
	 * 行为类对象
	 * @param actionName
	 * @return
	 */
	public HttpServletAction getAction(String actionName) {
		if (actionName == null || actionName.length() == 0) return null;
		return actions.get(actionName);
	}
	
	public void addAction(HttpServletAction action) {
		actions.put(action.getActionName(), action);
	}
	public HttpServletAction removeAction(String actionName) {
		return actions.remove(actionName);
	}

	@Override
	protected void post(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		try {
			logger.debug("http :"+req.getRemoteAddr()+"  session["+req.getSession().getId()+"] doPost .getContextPath():"+req.getContextPath());
			String actionName = req.getParameter(PARAMTER_KEY);
			HttpServletAction actionInstance = getAction(actionName);
			if (actionInstance == null)
			{
				writeResponse(resp,"error : use action="+actionName+" is not exist.");
				return;
			}
			actionInstance.doPost(req, resp);
		} catch (Exception e) {
			logger.error("http :"+req.getRemoteAddr()+"  session["+req.getSession().getId()+"] doPost is error.",e);
		}
	}

	
}

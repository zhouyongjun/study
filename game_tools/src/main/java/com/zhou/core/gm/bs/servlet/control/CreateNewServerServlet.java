package com.zhou.core.gm.bs.servlet.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhou.core.http.server.servlet.AbstractServlet;
import com.zhou.util.JsonUtil;

public class CreateNewServerServlet extends AbstractServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void post(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		req.setCharacterEncoding("UTF-8");
		Map<ServerParam,String> serverParams = new HashMap<ServerParam, String>();
		Map<String,String[]> params = req.getParameterMap();
		for (String param : params.keySet())
		{
			ServerParam sp = ServerParam.getValueOfParam(param);
			if (sp == null) continue;
			String value = req.getParameter(param);
			serverParams.put(sp, value);
		}
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().println(JsonUtil.toJson(serverParams));
		resp.getWriter().flush();
	}

	@Override
	protected void get(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		post(req, resp);
	}

	@Override
	public String getHttpServletName() {
		return "createNewServer";
	}

	@Override
	public String getPattern() {
		return "/control/newserver";
	}

}

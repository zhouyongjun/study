package com.zhou.controller.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class SessionController {
	
	@RequestMapping("/get")
	public String get(HttpServletRequest request)
	{
		String userName = (String) request.getSession().getAttribute("username");
		return "userName:"+userName;
	}
	
	@RequestMapping("/set")
	public Map<String,Object> index(HttpServletRequest request)
	{
		Map<String,Object> map = new HashMap<>();
		request.getSession().setAttribute("username", "admin"+(int)(Math.random() * 1000));
		map.put("sessionId", request.getSession().getId());
		map.put("userName", request.getSession().getAttribute("username"));
		return map;
	}
	
}

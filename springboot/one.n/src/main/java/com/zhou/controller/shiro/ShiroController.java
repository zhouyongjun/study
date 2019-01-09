package com.zhou.controller.shiro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.zhou.dao.shrio.ShiroUserBean;
import com.zhou.service.IShiroService;

@Controller
@RequestMapping("/shiro")
public class ShiroController {

	@Autowired
	IShiroService service;
	
	@ExceptionHandler(value = { RuntimeException.class })
	public String exception(Exception e, WebRequest webRequest) {
		return "出现异常了，在本类处理";
	}
	@RequestMapping("/index")
	public ModelAndView index()
	{
		ModelAndView model = new ModelAndView();
		model.setViewName("/shiro/index");
		List<ShiroUserBean> users = service.findAllUsers();
		model.addObject("users",users);
		return model;
	}
	
	@RequestMapping("/loginUrl")
	public String loginUrl()
	{
		return "/shiro/login";
	}
	
	@RequestMapping("/login")
	public ModelAndView login(@RequestParam String name,@RequestParam String password)
	{
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(name,password);
		try {
			subject.login(token);
		} catch (Exception e) {
			e.printStackTrace();
			return new ModelAndView("/shiro/errorShiro");
		}
		ModelAndView model = new ModelAndView();
		model.setViewName("/shiro/index");
		List<ShiroUserBean> users = service.findAllUsers();
		model.addObject("users",users);
		return model;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout()
	{
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		String name = subject.getPrincipal().toString();
		ModelAndView model = new ModelAndView();
		model.setViewName("/shiro/logout");
		model.addObject("user",name);
		return model;
	}
	/**
	 * 直接通过浏览器输入url时，@RequestBody获取不到json对象，需要用java编程或者基于ajax的方法请求，将Content-Type设置为application/json
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/addUser",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView addUser(@RequestParam("name")String name,@RequestParam("password")String password)
	{
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("password", password);
		service.addUser(map);
		ModelAndView model = new ModelAndView();
		model.setViewName("/shiro/index");
		List<ShiroUserBean> users = service.findAllUsers();
		model.addObject("users",users);
		return model;
	}
	
	@RequestMapping("/user/index")
	public ModelAndView users()
	{
		ModelAndView model = new ModelAndView();
		model.setViewName("/shiro/index");
		List<ShiroUserBean> users = service.findAllUsers();
		model.addObject("users",users);
		return model;
	}
	
}


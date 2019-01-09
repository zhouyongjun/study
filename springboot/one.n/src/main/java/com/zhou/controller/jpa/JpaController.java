package com.zhou.controller.jpa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zhou.dao.jpa.entity.UserBean;
import com.zhou.dao.jpa.mapper.UserBeanMapper;
import com.zhou.service.IUserService;

@Controller
@RequestMapping("/jpa")
public class JpaController {
	@Autowired
	IUserService service;
	@RequestMapping
	public ModelAndView index()
	{
		UserBean bean = service.findById(1l);
		try {
			service.saveUser(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		
		sb.append(bean == null ? "NULL" : bean.toString()).append("\n");
		bean = service.findByNameAndPassword("王二麻", "456");
		sb.append(bean == null ? "NULL" : bean.toString()).append("\n");
		List<UserBean> beanList = service.findByName("李四");
		sb.append(beanList == null ? "NULL" : beanList.size());
		System.err.println(sb.toString());
		
		//
		ModelAndView model = new ModelAndView("jpa/index");
		List<UserBean> list = service.findAll();
		model.addObject("users", list);
		return model;
	}
	@RequestMapping("/save")
	@ResponseBody
	public UserBean save()
	{
		UserBean bean = new UserBean();
		bean.setName("我去");
		bean.setPassword("1111111111");
		service.saveUser(bean);
		return bean;
	}
}

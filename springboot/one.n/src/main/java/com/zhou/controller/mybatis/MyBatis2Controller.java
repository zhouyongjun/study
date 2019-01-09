package com.zhou.controller.mybatis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhou.dao.jpa.entity.UserBean;
import com.zhou.dao.mybatis.MyBatis2UserBeanMapper;

@RestController
@RequestMapping("/mybatis2")
public class MyBatis2Controller {
	@Autowired
	MyBatis2UserBeanMapper mapper;
	
	@RequestMapping("/list")
	public List<UserBean> list()
	{
		List<UserBean> list = mapper.getUsers();
		return list;
	}
	@RequestMapping("/update")
	public UserBean update(@RequestParam long id,@RequestParam String name,@RequestParam String password)
	{
		UserBean userBean = mapper.getUserById(id);
		userBean.setName(name);
		userBean.setPassword(password);
		mapper.update(userBean);
		return userBean;
	}
	
	@RequestMapping("/del")
	public List<UserBean> del(@RequestParam long id)
	{
		mapper.del(id);;
		return mapper.getUsers();
	}
	
	@RequestMapping("/insert")
	public List<UserBean> insert(@RequestParam String name,@RequestParam String password)
	{
		UserBean userBean = new UserBean();
		userBean.setName(name);
		userBean.setPassword(password);
		mapper.save(userBean);
		return mapper.getUsers();
	}
	
	
}

package com.zhou.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhou.dao.jpa.entity.UserBean;
import com.zhou.dao.jpa.mapper.UserBeanMapper;
import com.zhou.service.IUserService;

@Service
public class UserServiceImpl implements IUserService{
	@Autowired
	UserBeanMapper mapper;
	
	@Transactional
	@Override
	public void saveUser(UserBean user) {
		user.setPassword(String.valueOf((int)(Math.random() * (10000))));
		System.err.println("getPassword:"+user.getPassword());
		mapper.save(user);//Save方法 可用于update和insert
//		 boolean flag = true;
//        if (flag) {//测试事务 
//        	throw new RuntimeException();
//        }
		
	}

	@Override
	public UserBean findById(Long id) {
		return mapper.findOne(id);
	}
	
	@Override
	public List<UserBean> findAll() {
		return mapper.findAll();
	}

	@Override
	public UserBean findByNameAndPassword(String name, String password) {
		return mapper.findByNameAndPassword(name, password);
	}

	@Override
	public List<UserBean> findByName(String name) {
		return mapper.findByName(name);
	}
	
}

package com.zhou.service;

import java.util.List;

import com.zhou.dao.jpa.entity.UserBean;

public interface IUserService {
	public void saveUser(UserBean user);
	public List<UserBean> findAll();
	public UserBean findByNameAndPassword(String name,String password);
	public List<UserBean> findByName(String name);
	public UserBean findById(Long id);
}

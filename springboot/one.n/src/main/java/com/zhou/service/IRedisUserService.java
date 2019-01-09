package com.zhou.service;

import com.zhou.dao.jpa.entity.UserBean;

public interface IRedisUserService {
	public UserBean getUserById(long id);
	public UserBean updateUser(UserBean user);
	public String del(long id);
	
}

package com.zhou.service;

import java.util.List;
import java.util.Map;

import com.zhou.dao.shrio.ShiroPermissionBean;
import com.zhou.dao.shrio.ShiroRoleBean;
import com.zhou.dao.shrio.ShiroUserBean;

public interface IShiroService {
	ShiroUserBean findByName(String name);
	
	public ShiroUserBean addUser(Map<String, Object> map);
	
	public ShiroRoleBean addRole(Map<String, Object> map);
	
	public ShiroPermissionBean addPermission(Map<String, Object> map);

	List<ShiroUserBean> findAllUsers();
}

package com.zhou.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhou.dao.jpa.mapper.ShiroPermissionBeanMapper;
import com.zhou.dao.jpa.mapper.ShiroRoleBeanMapper;
import com.zhou.dao.jpa.mapper.ShiroUserBeanMapper;
import com.zhou.dao.shrio.ShiroPermissionBean;
import com.zhou.dao.shrio.ShiroRoleBean;
import com.zhou.dao.shrio.ShiroUserBean;
import com.zhou.service.IShiroService;

@Service
public class ShiroServiceImpl implements IShiroService {
	@Autowired
	ShiroUserBeanMapper userMapper;
	@Autowired
	ShiroPermissionBeanMapper permissionMapper;
	@Autowired
	ShiroRoleBeanMapper roleMapper;
	@Override
	public ShiroUserBean findByName(String name) {
		return userMapper.findByName(name);
	}

	@Override
	public List<ShiroUserBean> findAllUsers() {
		return userMapper.findAll();
	}
	
	@Override
	public ShiroPermissionBean addPermission(Map<String, Object> map) {
		return null;
	}

	@Override
	public ShiroUserBean addUser(Map<String, Object> map) {
		ShiroUserBean user = new ShiroUserBean();
		user.setName(map.get("name").toString());
		user.setPassword(map.get("password").toString());
		return userMapper.save(user);
	}

	@Override
	public ShiroRoleBean addRole(Map<String, Object> map) {
		String name = map.get("name").toString();
		ShiroUserBean user = userMapper.findOne(Integer.parseInt(map.get("userId").toString()));
		ShiroRoleBean role = new ShiroRoleBean();
		role.setName(name);
		role.setUser(user);
		ShiroPermissionBean permission1 = new ShiroPermissionBean();
        permission1.setPermission("create");
        permission1.setRole(role);
        ShiroPermissionBean permission2 = new ShiroPermissionBean();
        permission2.setPermission("update");
        permission2.setRole(role);
        List<ShiroPermissionBean> permissions =  new ArrayList<ShiroPermissionBean>();
        permissions.add(permission1);
        permissions.add(permission2);
		role.setPermissions(permissions);
		return roleMapper.save(role);
	}
	
}

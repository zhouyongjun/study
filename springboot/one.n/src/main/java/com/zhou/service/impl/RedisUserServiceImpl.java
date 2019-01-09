package com.zhou.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.zhou.dao.jpa.entity.UserBean;
import com.zhou.dao.jpa.mapper.UserBeanMapper;
import com.zhou.service.IRedisUserService;

@CacheConfig(cacheNames="users")
@Service("redisService")
public class RedisUserServiceImpl implements IRedisUserService {
	@Autowired
	UserBeanMapper mapper;
	
	
	@Cacheable(key="'userCache'")
	@Override
	public UserBean getUserById(long id) {
		System.err.println("RedisUserServiceImpl 执行这里，说明缓存中读取不到数据，直接读取数据库....");
		UserBean ub = mapper.findOne(id);
		return ub;
	}

	@CachePut(key="'userCache'")
	@Override
	public UserBean updateUser(UserBean user) {
		 System.err.println("RedisUserServiceImpl 执行这里，更新数据库，更新缓存....");
		return mapper.save(user);
	}

	@CacheEvict(key="'userCache'")
	@Override
	public String del(long id) {
		 mapper.delete(id);
		return "RedisUserServiceImpl 删除成功！！！！";
	}

}

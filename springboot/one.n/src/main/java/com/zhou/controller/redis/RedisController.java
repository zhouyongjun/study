package com.zhou.controller.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhou.dao.jpa.entity.UserBean;
import com.zhou.service.IRedisUserService;

@RestController
@RequestMapping("/redis")
public class RedisController {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	@Qualifier("redisService")
	IRedisUserService service;
	@RequestMapping
	public String test()
	{
		redisTemplate.opsForValue().set("study", "Java");
		return redisTemplate.opsForValue().get("study");
	}
	@RequestMapping("/get")
	public UserBean get()
	{
		return service.getUserById(1);
	}
	
	@RequestMapping("/update")
	public UserBean update()
	{
		UserBean user = service.getUserById(1);
		user.setPassword(String.valueOf((int)(Math.random() * 10000)));
		return service.updateUser(user);
	}
	
	@RequestMapping("/del")
	public String del()
	{
		return service.del(1);
	}
	
}

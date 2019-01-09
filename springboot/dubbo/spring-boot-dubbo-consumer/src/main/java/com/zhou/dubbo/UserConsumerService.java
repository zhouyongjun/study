package com.zhou.dubbo;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhou.domain.entity.User;

@Component
public class UserConsumerService {

	@Reference(version="1.0.0")
	IUserService cityService;
	
	public User findUserByName(String name)
	{
		User user = cityService.findUserByName(name);
		System.out.println(user.toString());
		return user;
	}
}

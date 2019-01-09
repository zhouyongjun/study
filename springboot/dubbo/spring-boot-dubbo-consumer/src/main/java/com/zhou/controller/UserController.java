package com.zhou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhou.domain.entity.User;
import com.zhou.dubbo.UserConsumerService;

@RestController
public class UserController {
	@Autowired
	UserConsumerService service;
	
	@RequestMapping("/consumer")
	public User get(@RequestParam("name") String name)
	{
		return 	service.findUserByName(name);
	}
}

package com.zhou.dubbo.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhou.domain.entity.User;
import com.zhou.dubbo.IUserService;

@Component
@Service(version="1.0.0")
public class UserServiceImpl implements IUserService {

	@Override
	public User findUserByName(String cityName) {
		return new User(1l, 2l, cityName,cityName+" 是我的故乡");
	}

}

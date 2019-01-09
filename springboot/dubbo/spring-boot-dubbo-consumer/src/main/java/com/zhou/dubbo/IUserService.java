package com.zhou.dubbo;

import com.zhou.domain.entity.User;


public interface IUserService {
	User findUserByName(String cityName);
}

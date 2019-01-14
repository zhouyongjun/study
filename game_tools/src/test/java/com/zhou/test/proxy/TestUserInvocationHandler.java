package com.zhou.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class TestUserInvocationHandler {

	public static void main(String[] args) {
		UserSerivce userService = new UserServiceImpl();
		
		InvocationHandler invocationHandler = new UserInvocationHandler(userService);
		
		UserSerivce userSerivceProxy = (UserSerivce) Proxy.newProxyInstance(userService.getClass().getClassLoader(),
				userService.getClass().getInterfaces(), invocationHandler);
		System.out.println(userSerivceProxy.getName(1));
		System.out.println(userSerivceProxy.getAge(1));
	}
}

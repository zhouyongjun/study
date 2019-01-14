package com.zhou.test.proxy;

public class UserServiceImpl implements UserSerivce {

	@Override
	public String getName(int id) {
		  System.out.println("------getName------");
	        return "Tom";
	}

	@Override
	public Integer getAge(int id) {
		  System.out.println("------getAge------");
	        return 10;
	}

}

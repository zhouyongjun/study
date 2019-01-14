package com.zhou.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserInvocationHandler implements InvocationHandler {
	private Object target;
	public UserInvocationHandler() {
		super();
	}
	public UserInvocationHandler(Object target) {
		super();
		this.target = target;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		if("getName".equals(method.getName()))
		{
			System.out.println("++++++before " + method.getName()+" ++++++");
			result = method.invoke(target, args);
			System.out.println("++++++after " + method.getName()+" ++++++");
		}
		else
		{
			result = method.invoke(target, args);
		}
		return result;
	}

}

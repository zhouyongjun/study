package com.zhou.oldservlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.err.println("我是Listener contextInitialized");
	}
 
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.err.println("我是Listener contextDestroyed");
	}

}

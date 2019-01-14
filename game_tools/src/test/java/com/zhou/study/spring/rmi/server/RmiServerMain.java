package com.zhou.study.spring.rmi.server;

import java.io.File;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RmiServerMain {
	public static void main(String[] args) {
		String path = "rmiServer.xml";
		if(new File(path).exists())
		{
			System.out.println("exit");
		}
		new ClassPathXmlApplicationContext(path);
		Object lock = new Object();
		try {
			while(true)
			{
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

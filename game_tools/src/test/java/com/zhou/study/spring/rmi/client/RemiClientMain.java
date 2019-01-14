package com.zhou.study.spring.rmi.client;
import org.springframework.context.ApplicationContext;  
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhou.study.spring.rmi.server.GameService;
public class RemiClientMain {
	      
    public static void main(String[] args) {  
        ApplicationContext ctx = new ClassPathXmlApplicationContext("rmiClient.xml");  
        GameService accountService = (GameService) ctx.getBean("gameService");  
        System.err.println(accountService.login("test", "1111"));  
        System.err.println(accountService.logout("test"));
        System.err.println(accountService.cmd("Å£±Æ"));
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

package com.zhou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import com.zhou.dubbo.UserConsumerService;

@SpringBootApplication
@EnableDubboConfiguration
public class DubboConsumerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(DubboConsumerApplication.class, args);
//		UserConsumerService service = run.getBean(UserConsumerService.class);
		//测试
//		service.findUserByName("");
	}
}

package com.zhou.study.sprintboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class ApplicationMain {
	@RequestMapping("/")
	String index()
	{
		return "Welcome to know Spring Boot !";
	}
	public static void main(String[] args) {
		SpringApplication.run(ApplicationMain.class, args);
	}
}

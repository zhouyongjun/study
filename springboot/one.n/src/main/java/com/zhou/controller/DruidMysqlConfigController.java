package com.zhou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhou.config.DruidMySqlConfiguration;


@RestController
public class DruidMysqlConfigController {

	@Autowired
	private DruidMySqlConfiguration config;
	
	@RequestMapping("/druid-mysql/show")
	public DruidMySqlConfiguration show()
	{
		return config;
	}
}

package com.zhou.controller.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhou.service.IMailService;

@Controller
@RequestMapping("/mail")
public class MailController {
	
	@Autowired
	IMailService service;
	
	@RequestMapping
	@ResponseBody
	public String send()
	{
		service.send();
		return "发送成功";
		
	}
	
}

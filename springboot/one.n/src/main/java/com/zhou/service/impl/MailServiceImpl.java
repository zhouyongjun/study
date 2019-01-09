package com.zhou.service.impl;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.zhou.service.IMailService;

@Service
public class MailServiceImpl implements IMailService {

	@Autowired
	JavaMailSender sender;
	@Override
	public void send() {
		try {
			final MimeMessage mimeMessage = sender.createMimeMessage();
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
			message.setFrom("zhou.yj@joymeng.com");
			message.setTo("490658318@qq.com");
			message.setSubject("测试邮件主题");
			message.setText("测试邮件内容");
			sender.send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

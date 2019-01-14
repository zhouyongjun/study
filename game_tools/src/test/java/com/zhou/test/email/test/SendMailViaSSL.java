package com.zhou.test.email.test;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailViaSSL {

	 public static void main(String[] args) {
	  String host = "smtp.163.com";
	  String from = "发件地址";
	  String to = "收件地址";

	  final String userName = "发件用户名";
	  final String password = "发件密码";

	  Properties props = new Properties();
	  props.put("mail.smtp.host", host);
	  props.put("mail.smtp.socketFactory.port", "994");//不清楚为什么把这一行给注释掉也能够正常发送
	  props.put("mail.smtp.socketFactory.class",
	    "javax.net.ssl.SSLSocketFactory");//看起来，似乎只要有这一句就足够了。
	  props.put("mail.smtp.auth", "true");
	  props.put("mail.smtp.port", "465");//网易的SSL SMTP服务器的端口有465和994两个。

	  Session session = Session.getInstance(props, new Authenticator() {
	   protected PasswordAuthentication getPasswordAuthentication() {
	    return new javax.mail.PasswordAuthentication(userName, password);
	   }
	  });
	  try {
	   Message message = new MimeMessage(session);
	   message.setFrom(new InternetAddress(from));
	   message.setRecipients(Message.RecipientType.TO,
	     InternetAddress.parse(to));
	   message.setSubject("I have a dream, too!");
	   message.setText("I don't wanna be just a face in the crowd!");
	   Transport.send(message);
	   System.out.println("发送成功！");
	  } catch (MessagingException e) {
	   throw new RuntimeException(e);
	  }
	 }

}
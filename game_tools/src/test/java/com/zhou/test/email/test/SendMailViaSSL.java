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
	  String from = "������ַ";
	  String to = "�ռ���ַ";

	  final String userName = "�����û���";
	  final String password = "��������";

	  Properties props = new Properties();
	  props.put("mail.smtp.host", host);
	  props.put("mail.smtp.socketFactory.port", "994");//�����Ϊʲô����һ�и�ע�͵�Ҳ�ܹ���������
	  props.put("mail.smtp.socketFactory.class",
	    "javax.net.ssl.SSLSocketFactory");//���������ƺ�ֻҪ����һ����㹻�ˡ�
	  props.put("mail.smtp.auth", "true");
	  props.put("mail.smtp.port", "465");//���׵�SSL SMTP�������Ķ˿���465��994������

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
	   System.out.println("���ͳɹ���");
	  } catch (MessagingException e) {
	   throw new RuntimeException(e);
	  }
	 }

}
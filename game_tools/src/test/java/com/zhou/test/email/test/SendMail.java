package com.zhou.test.email.test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

	 public static void main(String[] args) {
	  String attachment = "C:" + File.separator + "scan.zip";

	  String to = "�ռ���";
	  String from = "������";
	  final String userName = "�����û���";
	  final String password = "��������";
	  String host = "smtp.163.com";

	  Properties props = new Properties();
	  props.put("mail.smtp.auth", "true");
	  props.put("mail.smtp.starttls.enable", "true");

	  props.put("mail.smtp.host", host);
	  props.put("mail.smtp.port", "25");

	  // ��ȡ�Ự����
	  Session session = Session.getInstance(props, new Authenticator() {
	   protected PasswordAuthentication getPasswordAuthentication() {
	    return new PasswordAuthentication(userName, password);
	   }
	  });

	  try {
	   Message message = new MimeMessage(session);
	   message.setFrom(new InternetAddress(from));
	   message.setRecipients(Message.RecipientType.TO,
	     InternetAddress.parse(to));
	   message.setSubject("Nice to meet you, JavaMail!");
	   BodyPart textBodyPart = new MimeBodyPart();
	   textBodyPart.setText("I have a dream!!!");
	   BodyPart messageBodyPart = new MimeBodyPart();
	   DataSource source = new FileDataSource(attachment);
	   messageBodyPart.setDataHandler(new DataHandler(source));

	   messageBodyPart.setFileName(Paths.get(attachment).getFileName()
	     .toString());

	   Multipart mp = new MimeMultipart();
	   mp.addBodyPart(textBodyPart);
	   mp.addBodyPart(messageBodyPart);
	   message.setContent(mp);
	   System.out.println("�ʼ�������");
	   Transport.send(message);
	   System.out.println("���ͳɹ���");

	  } catch (MessagingException e) {
	   throw new RuntimeException(e);
	  }
	 }
	
}

package com.zhou.test.email.test;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
public class InlinePicture {
 public static void main(String[] args) {
  String to = "zhou.yj@joymeng.com";
  String from = "letangcdn@163.com";
  final String userName = "letangcdn@163.com";
  final String password = "lt1632016";
  String host = "smtp.163.com";
  Properties props = new Properties();
  props.put("mail.smtp.auth", "true");
  props.put("mail.smtp.starttls.enable", "true");
  props.put("mail.smtp.host", host);
  props.put("mail.smtp.port", "25");
  Session session = Session.getInstance(props, new Authenticator() {
   protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(userName, password);
   }
  });
  try {
   Message message = new MimeMessage(session);
   message.setFrom(new InternetAddress(from));
   message.setRecipients(RecipientType.TO, InternetAddress.parse(to));
   message.setSubject("困兽");
   MimeMultipart multiPart = new MimeMultipart("related");
   BodyPart bp1 = new MimeBodyPart();
   String htmlText = "<H1>HELLO</H1><img src=\"cid:image\">";
   bp1.setContent(htmlText, "text/html");
   multiPart.addBodyPart(bp1);
   bp1 = new MimeBodyPart();
   DataSource ds = new FileDataSource("./target/test-classes/dongman1109.png");
   bp1.setDataHandler(new DataHandler(ds));
   bp1.setHeader("Content-ID", "<image>");
   multiPart.addBodyPart(bp1);
   message.setContent(multiPart);
   System.out.println("准备发送！");
   Transport.send(message);
   System.out.println("发送成功");
  } catch (MessagingException e) {
   throw new RuntimeException(e);
  }
 }
}
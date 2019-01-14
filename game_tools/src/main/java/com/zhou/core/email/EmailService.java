package com.zhou.core.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.ToolsApp;
import com.zhou.ToolsApp.ConsoleSystem;
import com.zhou.ToolsApp.SystemType;
import com.zhou.config.ToolsConfig;
import com.zhou.core.common.CTickable;
import com.zhou.core.task.DefaultScheduledPool;
import com.zhou.core.task.Taskable;
import com.zhou.util.CParam;
import com.zhou.util.CResult;
import com.zhou.util.StringUtils;
/**
 * �����ʼ����͹���
 *  ��������
 *    1��ָ����������+ָ��ʱ��:
 *       ����������ʱ����
 *       ��ʱ�䵽�������ۼƵı���
 *    2���ؼ����жϼ�ʱ����
 *       ƥ�䱨�����ƥ�䵽ָ�����ֶ���ʱ֪ͨ
 *      
 * @author zhouyongjun
 *
 */
public final class EmailService implements ConsoleSystem,CTickable {
	private static final Logger logger = LogManager.getLogger( EmailService.class);
	private static EmailService instance = new EmailService();
	private EmailService() {}
	protected Map<String,EmailListener> listeners = new HashMap<>();
	protected boolean isStartUp;
	private ScheduledFuture<?> minuteFuture;
	public static EmailService getInstance() {return instance;}
	Taskable minuteTaskable = new Taskable() {
			
			@Override
			public void run() {
				try {
	//				logger.debug("Console App minutely task run.");
					if (!EmailConfig.EMAIL_TICK_SWITCH)
					{
						return;
					}
					EmailService.getInstance().tick(CParam.newInstance());
				} catch (Throwable e) {
					logger.error(e);
				}
			}
		};
	/**
	 * ��������
	 * @throws Throwable
	 */
	public void startup(Object... params) throws Throwable
	{
		logger.info("Email Service is start up.");
		if (!ToolsApp.getInstance().isConfigLoaded())
		{
			ToolsApp.getInstance().loadConfigPropeties();
		}
		EmailConfig.load();
		minuteFuture = DefaultScheduledPool.getInstance().scheduleAtFixedRate(minuteTaskable, 0, EmailConfig.scheduleAtFixedRate_second, TimeUnit.SECONDS);
		isStartUp = true;
	}
	
	/**
	 * 
	 * @param type
	 * @param param ʹ�����õ�EmailListenerImpl�࣬������Ҫ2��Key�֣�CParam.newInstance().put("hint",hint).put("text",text);
	 * @return
	 */
	public CResult listen(String type,CParam param)
	{
		EmailListener listener = getListener(type);
		if (listener == null)
		{
			logger.error(this.getClass().getSimpleName() + " listen is error,type["+type+"] not exist in "+ToolsConfig.EMAIL_CONFIG_FILE_NAME);
		return CResult.RESULT_FAIL;
		}
		return listener.listen(param);
	}
	/**
	 * �ʼ����ͼ򵥵�����
	 * @param hint
	 * @param msg
	 * @throws Exception 
	 */
	public CResult sendEmail(EmailMsg entry,InternetAddress[] toMailUsers,boolean isDebug) throws Exception {
		if (toMailUsers == null || toMailUsers.length == 0) return CResult.RESULT_FAIL;
		if (EmailConfig.EMAIL_SENDED_USER == null || !isStartUp()) return CResult.RESULT_FAIL;
		final String sender = EmailConfig.EMAIL_SENDED_USER;
		//������������
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", EmailConfig.EMAIL_SENDED_SERVER_HOST);
		props.setProperty("mail.debug", isDebug+"");
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.port", EmailConfig.EMAIL_SENDED_PORT+"");
		if (EmailConfig.EMAIL_SENDED_PORT ==465)//ʹ��465�˿�����,AWS 25�˿ڲ��ȶ����Կ���ʹ��465 
		{
			final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			props.setProperty("mail.smtp.socketFactory.port", "465");
			props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
		}
//		props.setProperty("mail.smtp.starttls.enable", "true");
//		props.setProperty("mail.debug.quote", "true");
		
		
		//
		Authenticator auth = new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(sender, EmailConfig.EMAIL_SENDED_PSSWD);
			    }
		};
		//��ȡsession
//		Session.getDefaultInstance(props) : ʹ�ô˷������б���:553 ���
		Session session = Session.getInstance(props, auth);
		//��Ϣ������
		MimeMessage message = new MimeMessage(session);
		
		//�����ߵ�ַ����
		InternetAddress senderAddress = new InternetAddress(sender);
		message.setFrom(senderAddress);
		
		//���ܷ�ʽ����
		/*
		 * 163���䱨��
				com.sun.mail.smtp.SMTPSendFailedException: 554 DT:SPM 163 smtp4,DtGowAC39BkEKIxX2VoCAA�C.130S2 1468803076,please seehttp://mail.163.com/help/help_spam_16.htm?ip=xxxxx&hostid=smtp4&time=1468803076 *
				 
		 * �����ڴ����ӳ�Ϯ���Լ�һ��
		 */
		message.addRecipients(MimeMessage.RecipientType.CC, new InternetAddress[]{senderAddress});
		
		message.addRecipients(RecipientType.TO,toMailUsers);
		//ͷ��ͷ�ֶ�
		String hint = entry.getHint();
		message.setSubject( hint == null ? "hint" : hint,"utf-8");
		//��Ϣ��
		String text = entry.toText();
		char[] chats = text.toCharArray();
		if (chats.length > 10240)
		{
			text = new String(chats, 0, 10240) +"<<<<<<<to more message delete>>>>>>";
		}
		message.setText(text == null ? "text is null.." : text, "utf-8");
		//����
		Transport.send(message);
		logger.info("send to email["+StringUtils.join(toMailUsers, ";")+"] hint["+hint+"] text size["+entry.size()+"] successfully.");
		return CResult.RESULT_SUCC;
	
	}
	


	@Override
	public SystemType getConsoleSubSystemType() {
		return SystemType.EMAIL;
	}
	@Override
	public void shutdown(Object... params) throws Throwable {
		logger.info("Email Service is shut down.");
		if (!isStartUp) return;
		stopListeners();
		if (minuteFuture != null) minuteFuture.cancel(true);
		isStartUp = false;
	}
	
	@Override
	public CResult tick(CParam param) {
		for (EmailListener listener : new ArrayList<>(listeners.values()))
		{
			listener.tick(param);
		}
		return null;
	}
	
	public Map<String, EmailListener> getListeners() {
		return listeners;
	}
	
	public EmailListener getListener(String key) {
		return listeners.get(key.toLowerCase());
	}
	/**
	 * ע��emailLister�¼�
	 * @param key
	 * @param listener
	 * @return old_listener ������keyֵ��Ӧ�ϵ�listener����
	 */
	public EmailListener registListener(String key,EmailListener listener)
	{
		EmailListener old = null;
		if (listeners.containsKey(key)) old = listeners.get(key);
		listeners.put(key, listener);
		return old;
	}

	@Override
	public boolean isStartUp() {
		return isStartUp;
	}

	/**
	 * ֹͣ���м����
	 */
	public void stopListeners() {
		for (EmailListener listener : listeners.values()) 
		{
			listener.stop(null);
		}
		listeners.clear();
	}
}

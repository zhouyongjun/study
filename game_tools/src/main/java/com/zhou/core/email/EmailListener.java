package com.zhou.core.email;

import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import org.w3c.dom.Element;

import com.zhou.core.common.CListener;
import com.zhou.core.common.CTickable;
import com.zhou.core.email.listen.event.EmailEvent;
import com.zhou.core.email.listen.filter.cond.CondFilter;
import com.zhou.core.email.listen.filter.send.SendedFilter;
import com.zhou.util.CParam;
import com.zhou.util.CResult;

public interface EmailListener extends CListener, CTickable{
	
	/**
	 * �����ʼ�
	 * @param param
	 * @return
	 */
	CResult sendEmail(EmailMsg entry);
	
	/**
	 * ��ȡһ�ֻ�������ʼ����ʹ���
	 * @return
	 */
	int getLoop_maxnum();
	/**
	 * ����һ�ֻ�������ʼ����ʹ���
	 * @param maxnum
	 */
	void setLoop_maxnum(int maxnum);
	/**
	 * ��ȡһ���ֻ�CDʱ��
	 * @return
	 */
	int getLoop_cd_minutes();
	/**
	 * ����һ���ֻ�CDʱ��
	 * @return
	 */
	void setLoop_cd_minutes(int minute);
	/**
	 * ��ȡ��������list
	 * @return 
	 */
	List<CondFilter> getCondFilters();
	/**
	 * ������������list
	 * @return
	 */
	void setCondFilter(List<CondFilter> filters);
	/**
	 * ��ȡ���͹���list
	 * @return 
	 */
	List<SendedFilter> getSendedFilters();
	/**
	 * ��ȡ���͹���list
	 * @return 
	 */
	void setSendedFilter(List<SendedFilter> filters);
	/**
	 * ��ȡ�����ʼ���ַ��
	 * @return 
	 */
	InternetAddress[] getToMailUsers();
	/**
	 * ���ý����ʼ���ַ��
	 * @return 
	 */
	void setToMailUsers(InternetAddress[] toMailUsers);
	/**
	 * ��ȡ�����Ƿ�Ϊdebugģʽ
	 * @return 
	 */
	boolean isSendedDebug();
	
	/**
	 * ���÷����Ƿ�Ϊdebugģʽ
	 * @return 
	 */
	void setSendedDebug(boolean isDebug);
	/**
	 * ��������
	 * @return 
	 */
	public void load(Element e)throws Exception;
	/**
	 * �Ƿ��������һ�ֻ�
	 * @return
	 */
	public boolean canResetLoop();
	/**
	 * ����һ�ֻ�
	 */
	public void resetLoop();
	/**
	 * �Ƿ��Ѵﵽ���������
	 * @return
	 */
	public boolean isMaxOfSendedNum();
	/**
	 * 
	 * @return
	 */
	public Map<String,EmailMsg> getEmail_msgs();
	 /**
	  * ��ȡָ���ʼ���Ϣ
	  * @param key
	  * @return
	  */
	public EmailMsg getEmail_msg(String key);
	/**
	 * ������Ϸ��Ϣ
	 * @param key
	 * @param value
	 * @return
	 */
	public EmailMsg putEmail_msg(String key,EmailMsg value);
	/**
	 * ɾ���ʼ���Ϣ�����
	 * @param hint
	 * @return
	 */
	public EmailMsg removeEmailEntry(String hint);
	
	/**
	 * ��ȡ�¼�list
	 * @return
	 */
	public List<EmailEvent> getEvents();
	/**
	 * �����¼�list
	 * @param events
	 */
	public void setEvents(List<EmailEvent> events);
	/**
	 * �����¼�
	 * @param event
	 * @param param
	 * @return
	 */
	CResult handleEvent(EmailEvent event,CParam param);
	/**
	 * ����ָ���¼�
	 * @param event
	 * @return
	 */
	public boolean addEvent(EmailEvent event);
	/**
	 * ɾ��ָ���¼�
	 * @param event
	 * @return
	 */
	public boolean removeEvent(EmailEvent event);
	/**
	 * ��ȡ����
	 * @return
	 */
	public String getType();
	/**
	 * ��������
	 * @param type
	 */
	public void setType(String type);
	/**
	 * ֹͣ��ǰ����
	 * @param param
	 */
	void stop(CParam param);
	
}

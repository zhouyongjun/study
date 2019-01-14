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
	 * 发送邮件
	 * @param param
	 * @return
	 */
	CResult sendEmail(EmailMsg entry);
	
	/**
	 * 获取一轮回内最大邮件发送次数
	 * @return
	 */
	int getLoop_maxnum();
	/**
	 * 设置一轮回内最大邮件发送次数
	 * @param maxnum
	 */
	void setLoop_maxnum(int maxnum);
	/**
	 * 获取一次轮回CD时间
	 * @return
	 */
	int getLoop_cd_minutes();
	/**
	 * 设置一次轮回CD时间
	 * @return
	 */
	void setLoop_cd_minutes(int minute);
	/**
	 * 获取条件过滤list
	 * @return 
	 */
	List<CondFilter> getCondFilters();
	/**
	 * 设置条件过滤list
	 * @return
	 */
	void setCondFilter(List<CondFilter> filters);
	/**
	 * 获取发送过滤list
	 * @return 
	 */
	List<SendedFilter> getSendedFilters();
	/**
	 * 获取发送过滤list
	 * @return 
	 */
	void setSendedFilter(List<SendedFilter> filters);
	/**
	 * 获取接受邮件地址组
	 * @return 
	 */
	InternetAddress[] getToMailUsers();
	/**
	 * 设置接受邮件地址组
	 * @return 
	 */
	void setToMailUsers(InternetAddress[] toMailUsers);
	/**
	 * 获取发送是否为debug模式
	 * @return 
	 */
	boolean isSendedDebug();
	
	/**
	 * 设置发送是否为debug模式
	 * @return 
	 */
	void setSendedDebug(boolean isDebug);
	/**
	 * 加载数据
	 * @return 
	 */
	public void load(Element e)throws Exception;
	/**
	 * 是否可以重置一轮回
	 * @return
	 */
	public boolean canResetLoop();
	/**
	 * 重置一轮回
	 */
	public void resetLoop();
	/**
	 * 是否已达到最大发送数量
	 * @return
	 */
	public boolean isMaxOfSendedNum();
	/**
	 * 
	 * @return
	 */
	public Map<String,EmailMsg> getEmail_msgs();
	 /**
	  * 获取指定邮件信息
	  * @param key
	  * @return
	  */
	public EmailMsg getEmail_msg(String key);
	/**
	 * 增加游戏信息
	 * @param key
	 * @param value
	 * @return
	 */
	public EmailMsg putEmail_msg(String key,EmailMsg value);
	/**
	 * 删除邮件信息题对象
	 * @param hint
	 * @return
	 */
	public EmailMsg removeEmailEntry(String hint);
	
	/**
	 * 获取事件list
	 * @return
	 */
	public List<EmailEvent> getEvents();
	/**
	 * 设置事件list
	 * @param events
	 */
	public void setEvents(List<EmailEvent> events);
	/**
	 * 处理事件
	 * @param event
	 * @param param
	 * @return
	 */
	CResult handleEvent(EmailEvent event,CParam param);
	/**
	 * 增加指定事件
	 * @param event
	 * @return
	 */
	public boolean addEvent(EmailEvent event);
	/**
	 * 删除指定事件
	 * @param event
	 * @return
	 */
	public boolean removeEvent(EmailEvent event);
	/**
	 * 获取类型
	 * @return
	 */
	public String getType();
	/**
	 * 设置类型
	 * @param type
	 */
	public void setType(String type);
	/**
	 * 停止当前监听
	 * @param param
	 */
	void stop(CParam param);
	
}

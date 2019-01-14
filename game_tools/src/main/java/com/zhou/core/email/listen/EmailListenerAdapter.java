package com.zhou.core.email.listen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.mail.internet.InternetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.zhou.core.email.EmailConfig;
import com.zhou.core.email.EmailListener;
import com.zhou.core.email.EmailMsg;
import com.zhou.core.email.listen.event.EmailEvent;
import com.zhou.core.email.listen.event.timer.TimerEvent;
import com.zhou.core.email.listen.filter.cond.CondFilter;
import com.zhou.core.email.listen.filter.send.SendedFilter;
import com.zhou.util.CParam;
import com.zhou.util.CResult;
import com.zhou.util.CalendarUtil;
import com.zhou.util.CommonUtils;
import com.zhou.util.StringUtils;
import com.zhou.util.XmlUtils;
	/**
	 * 监听适配类
	 * @author zhouyongjun
	 *
	 */
public abstract class EmailListenerAdapter implements EmailListener {
	private static final Logger logger = LogManager.getLogger(EmailListenerAdapter.class);
	protected String type;//类型
	protected int loop_maxnum;//一次轮回内最大发送邮件次数
	protected int loop_cd_minutes;//一次轮回时间间隔
	protected List<CondFilter> condFilters;//记录过滤
	protected List<SendedFilter> sendedFilters;//发送过滤
	protected List<EmailEvent> events = new ArrayList<EmailEvent>();
	protected InternetAddress[] toMailUsers;//接受者邮箱
	public AtomicInteger loop_email_num = new AtomicInteger();
	protected boolean sended_debug;
	protected long next_reset_loop_date;
	protected Map<String,EmailMsg> email_msgs = new HashMap<>();//邮件内容
	protected boolean isStop;
	@Override
	public int getLoop_maxnum() {
		return loop_maxnum;
	}

	@Override
	public void setLoop_maxnum(int maxnum) {
		this.loop_maxnum = maxnum;
	}

	@Override
	public int getLoop_cd_minutes() {
		return loop_cd_minutes;
	}

	@Override
	public void setLoop_cd_minutes(int minute) {
		this.loop_cd_minutes = minute;
	}


	@Override
	public CResult tick(CParam param) {
		resetLoop();
		_tickOfTimerEvents();
		return null;
	}

	private void _tickOfTimerEvents() {
		try {
			List<TimerEvent> tes = getEventsByClass(TimerEvent.class);
			if (tes == null || tes.isEmpty()) return;
			for (TimerEvent event : tes) 
			{
				event.tick(null);
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
	}

	@Override
	public List<CondFilter> getCondFilters() {
		return condFilters;
	}

	@Override
	public void setCondFilter(List<CondFilter> filters) {
		this.condFilters = filters;
	}

	@Override
	public List<SendedFilter> getSendedFilters() {
		return sendedFilters;
	}

	@Override
	public void setSendedFilter(List<SendedFilter> filters) {
		this.sendedFilters = filters;
	}

	@Override
	public InternetAddress[] getToMailUsers() {
		return toMailUsers;
	}

	@Override
	public void setToMailUsers(InternetAddress[] toMailUsers) {
		this.toMailUsers = toMailUsers;
	}
	
	/**
	 * 执行条件过滤
	 * @param param
	 * @return 如果一条filter不满足则返回false的CResult对象
	 */
	public CResult handleCondFilter(CParam param)
	{
		if (condFilters == null) return CResult.RESULT_SUCC;
		for (CondFilter filter : condFilters)
		{
			CResult result = filter.filter(param);
			if (result.isFail()) return result;
		}
		return CResult.RESULT_SUCC;
	}

	/**
	 * 执行发送条件过滤
	 * @param param
	 * @return 如果一条filter满足则返回true的CResult对象
	 */
	public CResult handleSendedFilter(CParam param)
	{
		if (sendedFilters == null) return CResult.RESULT_SUCC;
		for (SendedFilter filter : sendedFilters)
		{
			CResult result = filter.filter(param);
			if (result.isSucc()) return result;
		}
		return CResult.RESULT_FAIL;
	}

	@Override
	public void load(Element e) throws Exception {
		loop_maxnum = StringUtils.getInt(XmlUtils.getAttribute(e, "loop_maxnum"));
		loop_cd_minutes = StringUtils.getInt(XmlUtils.getAttribute(e, "loop_cd_minutes"));
		sended_debug = StringUtils.isBoolean(XmlUtils.getAttribute(e, "sended_debug"));
		_loadCondFilters(XmlUtils.getChildrenByName(e, "condfilter"));
		_loadSendedFilters(XmlUtils.getChildrenByName(e, "sendfilter"));
		_loadReceivers(XmlUtils.getChildrenByName(e, "receiver"));
		_loadEvents(XmlUtils.getChildrenByName(e, "event"));
	}

	private void _loadEvents(Element[] els) throws Exception {
		List<EmailEvent> temps = new ArrayList<EmailEvent>();
		for (Element e : els) 
		{
			EmailEvent event = (EmailEvent) Class.forName(XmlUtils.getAttribute(e, "class_name")).newInstance();	
			event.load(CParam.newInstance().put("element", e).put("listener", this));
			temps.add(event);
		}
		this.events = temps;
	}

	private void _loadReceivers(Element[] els) throws Exception {
		List<InternetAddress> temps = new ArrayList<InternetAddress>();
		for (Element e : els) {
			temps.add(new InternetAddress(XmlUtils.getAttribute(e, "user")));
		}
		this.toMailUsers = new InternetAddress[temps.size()];
		temps.toArray(this.toMailUsers);	
	}

	@SuppressWarnings("unchecked")
	private void _loadSendedFilters(Element[] els) throws Exception {
		List<SendedFilter> filters = new ArrayList<SendedFilter>();
		for (Element e : els) {
			Class<? extends SendedFilter> clz = (Class<? extends SendedFilter>) Class.forName(XmlUtils.getAttribute(e, "class_name"));
			SendedFilter filter = clz.newInstance();
			filter.parse(XmlUtils.getAttribute(e, "param"));
			filters.add(filter);
		}
		this.sendedFilters = filters;
	}

	@SuppressWarnings("unchecked")
	private void _loadCondFilters(Element[] els) throws Exception {
		List<CondFilter> filters = new ArrayList<CondFilter>();
		for (Element e : els) {
			Class<? extends CondFilter> clz = (Class<? extends CondFilter>) Class.forName(XmlUtils.getAttribute(e, "class_name"));
			CondFilter filter = clz.newInstance();
			filter.parse(XmlUtils.getAttribute(e, "param"));
			filters.add(filter);
		}
		this.condFilters = filters;
	}

	@Override
	public boolean isSendedDebug() {
		return sended_debug;
	}

	@Override
	public void setSendedDebug(boolean isDebug) {
		this.sended_debug = isDebug;
	}
	
	public boolean canSend() {
		 return EmailConfig.EMAIL_SENDED_SWITCH && !isMaxOfSendedNum();
	 }
	
	 @Override
	public boolean canResetLoop() {
		return EmailConfig.EMAIL_SENDED_SWITCH && next_reset_loop_date < System.currentTimeMillis();
	}	
	@Override
	public void resetLoop() {
		try {
			if (!canResetLoop()) return;
			int num = loop_email_num.get();
			loop_email_num.set(0);
			next_reset_loop_date = System.currentTimeMillis() + getLoop_cd_minutes() * CalendarUtil.MINUTE;
			logger.debug("EmailListenerAdapter resetLoop before loop_email_num="+num+",after date :"+CalendarUtil.format(next_reset_loop_date));
		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	public boolean isMaxOfSendedNum() {
		return getLoop_maxnum() >= 0 && loop_email_num.get() >= getLoop_maxnum();
	}
	
	@Override
	public Map<String, EmailMsg> getEmail_msgs() {
		return email_msgs;
	}
	@Override
	public EmailMsg getEmail_msg(String hint) {
		return email_msgs.containsKey(hint) ? email_msgs.get(hint) : null;
	}
		
	@Override
	public EmailMsg putEmail_msg(String hint, EmailMsg value) {
		return email_msgs.put(hint, value);
	}
	
	@Override
	public EmailMsg removeEmailEntry(String hint) {
		return email_msgs.remove(hint);
	}
	
	
	@Override
	public List<EmailEvent> getEvents() {
		return events;
	}
	@Override
	public void setEvents(List<EmailEvent> events) {
		this.events = events;
	}
	
	@Override
	public boolean addEvent(EmailEvent event) {
		return events.add(event);
	}
	
	@Override
	public boolean removeEvent(EmailEvent event) {
		return events.remove(event);
	}
	
	@Override
	public CResult handleEvent(EmailEvent event, CParam param) {
		return null;
	}
	
	/**
	 * @获取指定clazz类的list
	 * @param clz
	 * @return
	 */
	public <T> List<T> getEventsByClass(Class<? extends T> clz)
	{
		return CommonUtils.getList(events, clz);
	}

	@Override
	public String getType() {
		return type;
	}
	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public void stop(CParam param) {
		isStop = true;
		for (EmailMsg entry : getEmail_msgs().values())
		{
			sendEmail(entry);
		}
		email_msgs.clear();
	}
}

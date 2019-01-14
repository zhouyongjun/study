package com.zhou.core.email.listen.event.timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;

import com.zhou.core.common.CTickable;
import com.zhou.core.email.EmailListener;
import com.zhou.core.email.EmailService;
import com.zhou.core.email.listen.event.EmailEvent;
import com.zhou.util.CParam;
import com.zhou.util.CResult;
import com.zhou.util.CalendarUtil;
import com.zhou.util.StringUtils;
import com.zhou.util.XmlUtils;

public abstract class TimerEvent implements EmailEvent,CTickable{
	public static final int DEFAULT_MINUTE = 1;	
	protected int interval_minute = DEFAULT_MINUTE;
	protected long next_tick_date;
	protected String showName;
	protected EmailListener listener;
	private static final Logger logger = LogManager.getLogger(TimerEvent.class);
	/**
	 * load提供
	 *   1)showName ：名次
	 *   2)listener : 对象
	 *   3)interval_minute : 时间间隔
	 */
	@Override
	public void load(CParam param) throws Exception {

		Element element = param.get("element", Element.class);
		if (element != null)
		{
			interval_minute = StringUtils.getInt(XmlUtils.getAttribute(element, "interval_minute"));
			showName = XmlUtils.getAttribute(element, "showName");
			listener = param.get("listener",EmailListener.class);
		}else 
		{
			interval_minute = param.get("interval_minute",Integer.class);
			showName = param.get("name",String.class);
			listener = param.get("listener",EmailListener.class);
		}
		_load(param);
		next_tick_date = System.currentTimeMillis() + interval_minute * CalendarUtil.MINUTE;
	}
	public abstract void _load(CParam param) throws Exception;
	@Override
	public CResult tick(CParam param) {
		try {
			if (!canTickable()) return null;
			handle(param);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		setNextTickDate(System.currentTimeMillis() + CalendarUtil.MINUTE * getIntervalMinute());
		return null;
	}
	

	public int getIntervalMinute() {
		return interval_minute;
	}

	public void setIntervalMinute(int interval_minute) {
		this.interval_minute = interval_minute;
	}

	public long getNextTickDate() {
		return next_tick_date;
	}

	public void setNextTickDate(long next_tick_date) {
		this.next_tick_date = next_tick_date;
	}

	public boolean canTickable() {
		if (!EmailService.getInstance().isStartUp()) return false;
//		if (System.currentTimeMillis() < OSService.getProcessStartTime() + CalendarUtil.MINUTE) return false;
		return next_tick_date <= System.currentTimeMillis();
	}

 @Override
	public String getShowName() {
		return showName;
	}
 
 	@Override
	public EmailListener getListener() {
		return this.listener;
	}
}

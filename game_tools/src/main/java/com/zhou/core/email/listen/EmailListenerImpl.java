package com.zhou.core.email.listen;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.email.EmailMsg;
import com.zhou.core.email.EmailService;
import com.zhou.util.CParam;
import com.zhou.util.CResult;
	/**
	 * 错误监管对象
	 * @author zhouyongjun
	 *
	 */
public class EmailListenerImpl extends EmailListenerAdapter  {
	private static final Logger logger = LogManager.getLogger(EmailListenerImpl.class);
	
	/**
	 * 邮件发送简单的内容
	 * @param hint
	 * @param msg
	 */
	public CResult sendEmail(EmailMsg entry) {
		try {
			logger.debug("EmailListenerImpl[usenum="+loop_email_num.get()+",maxnum="+loop_maxnum+"] sendEmail "+entry.toDetailString());
			if (!isStop && !canSend())
				{
					return CResult.RESULT_FAIL;
				}
			if (EmailService.getInstance().sendEmail(entry, toMailUsers,isSendedDebug()).isSucc())
			{
				removeEmailEntry(entry.getHint());
			}
			loop_email_num.addAndGet(1);
		}catch (Exception e) {
			logger.error("sendEmail["+entry+"] is error!",e);
		}
		return CResult.RESULT_SUCC;
	}
	/**
	 * tick 执行
	 */
	public CResult tick(CParam param) {
		try {
			super.tick(param);
			if (!canSend()) return CResult.RESULT_FAIL;
			for (EmailMsg entry : new ArrayList<>(email_msgs.values())) {
				if (entry.isEmpty()) continue;
				if (handleSendedFilter(CParam.newInstance().put("num", entry.getTexts().size())).isFail()) return CResult.RESULT_FAIL;
				sendEmail(entry);
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return CResult.RESULT_SUCC;
		
	}

	@Override
	public CResult listen(CParam param) {
		String hint = param.get("hint", String.class);
		String text = param.get("text", String.class);
		try {
			if (hint == null || text == null || text.isEmpty()) return CResult.createFailResult("记录字符串为空");
			CResult result = handleCondFilter(param);
			if (result.isFail()) return result;
			
			EmailMsg entry = getEmail_msg(hint);
			if (entry == null) {
				entry = new EmailMsg(hint);
				putEmail_msg(hint, entry);
			}
			entry.addText(text);
			logger.info("EmailListenerImpl listen "+entry.toString()+" some text :"+text.substring(0, Math.min(text.length(),1024)));
//			if (handleSendedFilter(param.put("num", entry.getTexts().size())).isSucc())
//			{
//				sendEmail(entry);
//			}
			return CResult.RESULT_SUCC;
		} catch (Exception e) {
			logger.error("recordEmailText hint["+hint+"] text["+text+"] is error.",e);
			return CResult.RESULT_FAIL;
		}
	}

}

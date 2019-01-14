package com.zhou.test.telnet;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.email.EmailService;
import com.zhou.core.os.OSService;
import com.zhou.core.script.ScriptService;
import com.zhou.core.telnet.TelnetHandle;
import com.zhou.util.CParam;

	/**
	 * @author zhouyongjun
	 *
	 */
public class ConsoleServiceImpl  implements TelnetHandle {
	private static final Logger logger = LogManager.getLogger(ConsoleServiceImpl.class);
	@Override
	public String handle(String msg) {
		logger.info("console service param : " + msg);
		StringBuffer result = new StringBuffer(1024);
		int index = msg.indexOf(" ");
		String cmd = index > 0 ? msg.substring(0, index).trim() : msg;
		
		List<String> outs = new ArrayList<String>();
		try {
			switch(cmd)
			{
			case "script":
			{
				String param = index > 0 ? msg.substring(index + 1).trim() : "";
				ScriptService.getInstance().runScript(param, outs);
				break;
			}
			case "osinfo":
			{
				outs.add(""+EmailService.getInstance().listen("OSINFORMATION", CParam.newInstance().put("hint","OS Information").put("text", OSService.showOSInformation())).isSucc());
				break;
			}
			case "email":
			{
				outs.add(""+EmailService.getInstance().listen("error", CParam.newInstance().put("hint","error").put("text", "test is error...")).isSucc());
				break;
			}
			}
					
		} catch (Exception e) {
			logger.error("console service handle["+msg+"] is error...",e);
			result.append("console service handle["+msg+"] is error..."+e.getMessage());
		}
		return result.toString();
	}

	
}

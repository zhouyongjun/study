package com.zhou.test.email;

import com.zhou.ToolsApp;
import com.zhou.core.email.EmailService;
import com.zhou.test.telnet.ConsoleServiceImpl;

public class EmailMain {

	public static void main(String[] args) {
		try {
			EmailService.getInstance().startup();
			ToolsApp.getInstance().getTelnetService().startup();
			ToolsApp.getInstance().getTelnetService().startConsole(20000, new ConsoleServiceImpl());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}

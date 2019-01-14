package com.zhou.test.telnet;

import com.zhou.ToolsApp;

public class TelnetMain {
	public static void main(String[] args) {
		try {
			ToolsApp.getInstance().getTelnetService().startup();
			ToolsApp.getInstance().getTelnetService().startConsole(20001, new ConsoleServiceImpl());
		}  catch (Throwable e) {
			e.printStackTrace();
		}
	}
}

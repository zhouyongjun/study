package com.zhou.test.gm;

import com.zhou.core.gm.cs.core.server.GmServer;
import com.zhou.core.gm.cs.core.server.console.DefaultGmManager;

public class GmServerMain {
	public static void main(String[] args) {
		try {
			GmServer.getInstance().startup(new DefaultGmManager());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}

package com.zhou.core.gm.cs.core.client;

import com.zhou.ToolsApp.ConsoleSystem;
import com.zhou.ToolsApp.SystemType;
import com.zhou.core.gm.cs.core.client.console.filter.GmClientHandleRegister;

public class GmClient implements ConsoleSystem{
	protected boolean isStartUp;
	private GmClient() {
		
	}
	public static GmClient getInstance() {
		return new GmClient();
	}
	@Override
	public SystemType getConsoleSubSystemType() {
		return SystemType.GM_CLIENT;
	}
	@Override
	public void startup(Object... params) throws Throwable {
		GmClientHandleRegister register = (GmClientHandleRegister) params[0];
		register.registerAllHandles();
		isStartUp = true;
	}
	@Override
	public void shutdown(Object... params) throws Throwable {
		isStartUp = false;
	}
	
	@Override
	public boolean isStartUp() {
		return isStartUp;
	}
}

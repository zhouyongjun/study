package com.zhou.core.telnet;

import com.zhou.ToolsApp;
import com.zhou.ToolsApp.ConsoleSystem;

public abstract class TelnetService implements ConsoleSystem
{
	protected boolean isStartUp;
	public abstract void startConsole(int paramInt, TelnetHandle paramConsoleService)throws Exception;
	public abstract void stopConsole(int port)throws Exception;
	public abstract void handleWhenStartup(Object... params)throws Throwable;
	public final void startup(Object... params)throws Throwable
	{
		if (!ToolsApp.getInstance().isConfigLoaded())
			ToolsApp.getInstance().loadConfigPropeties();
		 handleWhenStartup(params);
		 isStartUp = true;
	}
	@Override
	public void shutdown(Object... params) throws Throwable {
		isStartUp =false;
	}
   @Override
	public boolean isStartUp() {
		return isStartUp;
	}
}


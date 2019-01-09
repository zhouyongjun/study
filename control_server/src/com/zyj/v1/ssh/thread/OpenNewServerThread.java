package com.zyj.v1.ssh.thread;

import com.zyj.v1.SSHMain;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;

public class OpenNewServerThread extends Thread{
	public static Boolean isRun = false;
	Server newServer;
	Server oldServer;
	public OpenNewServerThread(Server newServer,Server oldServer) {
		this.newServer = newServer;
		this.oldServer = oldServer;
	}
	@Override
	public void run() {
		try {
			if (isRun) {
				SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(newServer, "已经开新区线程！！");
				return;
			}
			isRun = true;
			SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(newServer, "开区线程启动开始！");
			newServer.getAgent_new_server().open_new_server(oldServer,null);
			SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(newServer, "开区线程正常结束！");
		} catch (Exception e) {
			AppLog.error(e);
			SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(newServer, "开区线程异常结束！");
		}finally {
			isRun = false;
		}
	}
	
}

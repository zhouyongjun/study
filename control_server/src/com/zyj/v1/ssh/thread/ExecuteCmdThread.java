package com.zyj.v1.ssh.thread;

import com.zyj.v1.SSHMain;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.online_server.ServerState;
	/**
	 * ������ִ�������߳�
	 * @author zhouyongjun
	 *
	 */
public class ExecuteCmdThread extends Thread {
	Server server;
	ServerState exec_state;
	public ExecuteCmdThread(Server server,ServerState exec_state) {
		this.server = server;
		this.exec_state = exec_state;
	}
	@Override
	public void run() {
		switch(exec_state) {
		case START_UP:
		{
			server.getAgent_online_server().ssh_start_up_server();
			break;
		}
		case SHUT_DOWN:
		{
			server.getAgent_online_server().ssh_shutdown();
			break;
		}
		case FREEZE:
		{
			server.getAgent_online_server().telnet_freeze();
			break;
		}
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,exec_state.getName()+"�߳������رգ�����");
		if (server.getAgent_online_server().isExecSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(server,"***["+exec_state.getName()+"]ִ�гɹ�***");
		}
	}

}

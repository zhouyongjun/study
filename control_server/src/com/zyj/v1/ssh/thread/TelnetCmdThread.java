package com.zyj.v1.ssh.thread;

import java.util.List;

import com.zyj.v1.SSHMain;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.common.ExecuteResult;
	/**
	 * ������ִ�������߳�
	 * @author zhouyongjun
	 *
	 */
public class TelnetCmdThread extends Thread {
	Server server;
	List<CmdInfo> list;
	public TelnetCmdThread(Server server,List<CmdInfo> list) {
		this.server = server;
		this.list = list;
	}
	@Override
	public void run() {
		try {
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"��������̨�����߳̿���������");
			ExecuteResult result = server.getAgent_online_server().cmd_telnet(list);
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"��������̨�����߳���������������");
		} catch (Exception e) {
			AppLog.error(server, e);
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"��������̨�����߳��쳣����������");
		}
	}

}

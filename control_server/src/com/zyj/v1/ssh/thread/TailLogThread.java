package com.zyj.v1.ssh.thread;

import com.zyj.v1.SSHMain;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.common.ExecuteResult;
	/**
	 * ��־�߳�
	 * @author zhouyongjun
	 *
	 */
public class TailLogThread extends Thread{
	Server server;
	public TailLogThread(Server server) {
		this.server = server;
	}
	@Override
	public void run() {
		//ִ������
		CmdInfo cmd = server.getAgent_online_server().getCmd_tail_log();
		ExecuteResult result = server.getNet().getSsh_log().execCmd(cmd);
		if(!result.isSucc()) {
			ExecuteResult.createFailResult(server.getName()+","+result.getMsg()+"/n"+cmd.getShowName()+"����ʧ��...");
			return;
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"��־�߳������رգ�����");
	}

}

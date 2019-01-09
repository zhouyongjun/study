package com.zyj.v1.ssh.thread;

import java.util.List;

import com.zyj.v1.SSHMain;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.online_server.UpLoadFileGroup;
	/**
	 * �ϴ���Դ�߳�
	 * @author zhouyongjun
	 *
	 */
public class UploadThread extends Thread{
	OnlineServerManager sshMgr = OnlineServerManager.getInstance();
	Server mainServer;
	List<Server> select_servers;
	UpLoadFileGroup upload_files;
	boolean isTelnetLoad;
	public UploadThread(Server mainServer,List<Server> select_servers,UpLoadFileGroup upload_files,boolean isTelnetLoad) {
		this.mainServer = mainServer;
		this.select_servers = select_servers;
		this.upload_files = upload_files;
		this.isTelnetLoad = isTelnetLoad;
	}
	@Override
	public void run() {
		try {
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, "�ϴ���Դ�߳̿�������");
			sshMgr.upLoadZip(sshMgr.getMainServer(),select_servers,upload_files,isTelnetLoad);
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, "�ϴ���Դ�߳�������������");
		} catch (Exception e) {
			AppLog.error(e);
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(mainServer, "�ϴ���Դ�߳��쳣��������");
		}
	}
	
}

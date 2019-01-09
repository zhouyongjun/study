package com.zyj.v1.ssh.common;

import com.zyj.v1.SSHMain;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.thread.TailLogThread;

	/**
	 * ����ͻ���
	 * @author zhouyongjun
	 *
	 */
public class ClientNet {
	ClientTelnet telnet;//telent����
	ClientSSH ssh_log;//ssh��־
	ClientSSH ssh_cmd;//ssh����
	TailLogThread tail_log_thread;
	String liunx_time;
	boolean isCanNeedControlLog;
	Server server;
	public ClientNet(Server server) {
		this.server = server;
	}
	public ClientTelnet getTelnet() {
		return telnet;
	}
	public void setTelnet(ClientTelnet telnet) {
		this.telnet = telnet;
	}
	public ClientSSH getSsh_log() {
		return ssh_log;
	}
	public void setSsh_log(ClientSSH ssh_log) {
		this.ssh_log = ssh_log;
	}
	public ClientSSH getSsh_cmd() {
		return ssh_cmd;
	}
	public void setSsh_cmd(ClientSSH ssh_cmd) {
		this.ssh_cmd = ssh_cmd;
	}
	/**
	 * cmd �Ƿ����ӿ���
	 * @return
	 */
	public boolean isConnectCmdSSH() {
		return ssh_cmd != null  && ssh_cmd.isConnect();
	}
	/**
	 * telnet�Ƿ����ӿ���
	 * @return
	 */
	public boolean isConnectTelnet() {
		return telnet != null  && telnet.isConnect();
	}
	
	/**
	 * log�Ƿ����ӿ���
	 * @return
	 */
	public boolean isConnectCmdLog() {
		return ssh_log != null  && ssh_log.isConnect();
	}
	
	/**
	 * ����ssh cmd ����
	 * @param server
	 */
	public boolean openSsh_cmd_connect() {
		//���ԭ���Ĵ��ڣ���ر�ԭ����
		if (ssh_cmd != null) {
			ssh_cmd.closeConnection();
		}
		ssh_cmd = new ClientSSH(server,false);
		return ssh_cmd.openConnection() != null;
	}
	
	
	/**
	 * ����ssh cmd ����
	 * @param server
	 */
	public boolean openSsh_log_connect() {
		//���ԭ���Ĵ��ڣ���ر�ԭ����
		if (ssh_log != null) {
			ssh_log.closeConnection();
		}
		ssh_log = new ClientSSH(server,true);
		return ssh_log.openConnection() != null;
	}
	/**
	 * ������־�߳�
	 * @param server
	 */
	public void runTailLogThread(Server server) {
		//��ȡ��������ǰ���ڣ�
		getRunTimeFromServer(server);
		tail_log_thread = new TailLogThread(server);
		tail_log_thread.start();
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"��־�߳�����������");
	}
	
	private ExecuteResult getRunTimeFromServer(Server server) {
		ExecuteResult result = ssh_log.execCmd(new CmdInfo("date +%H:%M:%S", "��ȡ��������ǰʱ���"));
		if (result.isFail()) {
			return result;
		}
		liunx_time = result.getMsg();
		return result;
	}
	/**
	 * ��telnet����
	 * @return
	 */
	public boolean openTelnet() {
		return openTelnet(true);
	}
	
	/**
	 * ��telnet����
	 * @return
	 */
	public boolean openTelnet(boolean isshow) {
		if (telnet != null) {
			telnet.disconect();
		}
		telnet = new ClientTelnet(server);
		return telnet.connection(server.getTelnet_host(), server.getTelnet_port(),isshow);
	}
	public void close() {
		if (isConnectTelnet()) {
			telnet.disconect();
		}
		if (isConnectCmdSSH()) {
			ssh_cmd.closeConnection();
		}
		if (isConnectCmdLog()) {
			ssh_log.closeConnection();
		}
	}
	
	
}

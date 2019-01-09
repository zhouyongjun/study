package com.zyj.v1.ssh.common;

import com.zyj.v1.SSHMain;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.thread.TailLogThread;

	/**
	 * 网络客户端
	 * @author zhouyongjun
	 *
	 */
public class ClientNet {
	ClientTelnet telnet;//telent链接
	ClientSSH ssh_log;//ssh日志
	ClientSSH ssh_cmd;//ssh命令
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
	 * cmd 是否连接开启
	 * @return
	 */
	public boolean isConnectCmdSSH() {
		return ssh_cmd != null  && ssh_cmd.isConnect();
	}
	/**
	 * telnet是否连接开启
	 * @return
	 */
	public boolean isConnectTelnet() {
		return telnet != null  && telnet.isConnect();
	}
	
	/**
	 * log是否连接开启
	 * @return
	 */
	public boolean isConnectCmdLog() {
		return ssh_log != null  && ssh_log.isConnect();
	}
	
	/**
	 * 开启ssh cmd 连接
	 * @param server
	 */
	public boolean openSsh_cmd_connect() {
		//如果原来的存在，则关闭原来的
		if (ssh_cmd != null) {
			ssh_cmd.closeConnection();
		}
		ssh_cmd = new ClientSSH(server,false);
		return ssh_cmd.openConnection() != null;
	}
	
	
	/**
	 * 开启ssh cmd 连接
	 * @param server
	 */
	public boolean openSsh_log_connect() {
		//如果原来的存在，则关闭原来的
		if (ssh_log != null) {
			ssh_log.closeConnection();
		}
		ssh_log = new ClientSSH(server,true);
		return ssh_log.openConnection() != null;
	}
	/**
	 * 运行日志线程
	 * @param server
	 */
	public void runTailLogThread(Server server) {
		//获取服务器当前日期：
		getRunTimeFromServer(server);
		tail_log_thread = new TailLogThread(server);
		tail_log_thread.start();
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"日志线程启动！！！");
	}
	
	private ExecuteResult getRunTimeFromServer(Server server) {
		ExecuteResult result = ssh_log.execCmd(new CmdInfo("date +%H:%M:%S", "获取服务器当前时间点"));
		if (result.isFail()) {
			return result;
		}
		liunx_time = result.getMsg();
		return result;
	}
	/**
	 * 打开telnet连接
	 * @return
	 */
	public boolean openTelnet() {
		return openTelnet(true);
	}
	
	/**
	 * 打开telnet连接
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

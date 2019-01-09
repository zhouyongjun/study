package com.zyj.v1.ssh.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.commons.net.telnet.TelnetClient;

import com.zyj.v1.SSHMain;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
/**
	 * 本地telent链接
	 * @author zhouyongjun
	 *
	 */
public class ClientTelnet {
	Server server;
	TelnetClient client;//telnet链接类
	PrintStream pw;//写流
	BufferedReader br;//读流
	public ClientTelnet(Server server) {
		this.server = server; 
	}
	/**
	 * 从服务器读取命令
	 */
	public boolean receiveMsgFromServer() {
		String line = null;
		 try {
			while ((line = br.readLine()) != null) {
				AppLog.info(server,toString()+line);
				if (line.trim().equals(OnlineServerManager.TELNET_END_INFO)) {
					return true;
				}
			 }
			return true;
		} catch (Exception e) {
			AppLog.error(server,"receiveMsgFromServer["+line+"] is error",e);
			return false;
		}
	}
	
	/**
	 * 向服务器写命令
	 * @param cmd
	 */
	public ExecuteResult sendCmdToServer(CmdInfo cmd) {
		try {
			AppLog.info(server,"telnet send cmd :" + cmd);
			pw.println(cmd.getCmd());
			pw.flush();
			return ExecuteResult.createSuccResult("成功");
		} catch (Exception e) {
			AppLog.error(server,toString()+" sendCmdToServer["+cmd+"] is error...",e);
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"保存数据命令向服务器发送信息报错，执行["+cmd.getShowName()+"失败，请尽快质询相关服务器人员。。。");
			server.getAgent_online_server().setTelnetReturnSucc(false);
			return ExecuteResult.createFailResult("失败");
		}
	}
	/**
	 * 执行命令，包括发送和接受消息
	 * @param cmd
	 */
	public ExecuteResult execCmd(CmdInfo cmd) {
		ExecuteResult result = sendCmdToServer(cmd);
		if (!result.isSucc()) {
			return result;
		}
		if (receiveMsgFromServer()) {
			server.getAgent_online_server().setTelnetReturnSucc(true);
			if (SSHMain.mainFrame != null)  {
				if (SSHMain.mainFrame.getPanel_operate_online_server().getOperateType() == OperateType.SERVER_MAINTAIN && server.getAgent_online_server().isExecSucc()) {
					SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(server,"***["+server.getAgent_online_server().getState().getName()+"]执行成功***");
				}
			}
			return ExecuteResult.createSuccResult("成功");
		}
		server.getAgent_online_server().setTelnetReturnSucc(false);
		return ExecuteResult.createFailResult("失败");
	}
	
	public void disconect() {
		try {
			if (pw != null) {
				pw.close();	
			}
			if (pw != null) {
				br.close();
			}
			if (client != null) {
				client.disconnect();				
			}
			pw = null;
			br = null;
			client = null;
		} catch (IOException e) {
			AppLog.error(server,toString() + " disconect is error...",e);
		}
	}
	
	
	public boolean connection(String host,int port,boolean isShow) {
		client = new TelnetClient();
		try {
			AppLog.info(server,"telnet connect host["+host+"] port["+port+"] ....");
			client.connect(host, port);
			AppLog.info(server,"telnet isConnected :" + client.isConnected());
			pw = new PrintStream(client.getOutputStream());
			br = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF-8"));
			return receiveMsgFromServer();
		} catch (Exception e) {
			AppLog.error(server, toString() +" connection is error...", e);
			if (isShow && SSHMain.mainFrame != null) {
				SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"TELNET链接服务器失败，服务器已关闭或网络错误，质询相关服务器人员。。。");	
			}
			
			return false;
		}
	}

	public boolean isConnect() {
		return client != null && client.isConnected();
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	public TelnetClient getClient() {
		return client;
	}
	public void setClient(TelnetClient client) {
		this.client = client;
	}
	public PrintStream getPw() {
		return pw;
	}
	public void setPw(PrintStream pw) {
		this.pw = pw;
	}
	public BufferedReader getBr() {
		return br;
	}
	public void setBr(BufferedReader br) {
		this.br = br;
	}
	
	public String toString() {
		return "TELNET"; 
	}
	
}

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
	 * ����telent����
	 * @author zhouyongjun
	 *
	 */
public class ClientTelnet {
	Server server;
	TelnetClient client;//telnet������
	PrintStream pw;//д��
	BufferedReader br;//����
	public ClientTelnet(Server server) {
		this.server = server; 
	}
	/**
	 * �ӷ�������ȡ����
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
	 * �������д����
	 * @param cmd
	 */
	public ExecuteResult sendCmdToServer(CmdInfo cmd) {
		try {
			AppLog.info(server,"telnet send cmd :" + cmd);
			pw.println(cmd.getCmd());
			pw.flush();
			return ExecuteResult.createSuccResult("�ɹ�");
		} catch (Exception e) {
			AppLog.error(server,toString()+" sendCmdToServer["+cmd+"] is error...",e);
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"�������������������������Ϣ����ִ��["+cmd.getShowName()+"ʧ�ܣ��뾡����ѯ��ط�������Ա������");
			server.getAgent_online_server().setTelnetReturnSucc(false);
			return ExecuteResult.createFailResult("ʧ��");
		}
	}
	/**
	 * ִ������������ͺͽ�����Ϣ
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
					SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(server,"***["+server.getAgent_online_server().getState().getName()+"]ִ�гɹ�***");
				}
			}
			return ExecuteResult.createSuccResult("�ɹ�");
		}
		server.getAgent_online_server().setTelnetReturnSucc(false);
		return ExecuteResult.createFailResult("ʧ��");
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
				SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"TELNET���ӷ�����ʧ�ܣ��������ѹرջ����������ѯ��ط�������Ա������");	
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

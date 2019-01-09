package com.zyj.v1.ssh.online_server;

import java.util.List;

import com.zyj.v1.SSHMain;
import com.zyj.v1.common.Instances;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.ClientSSH;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.common.ExecuteResult;
import com.zyj.v1.ssh.common.ServerAgent;
import com.zyj.v1.ssh.thread.ExecuteCmdThread;
import com.zyj.v1.ssh.util.Const;
import com.zyj.v1.ssh.util.SshUtil;

public class OnlineServerAgent extends ServerAgent implements Instances{
	/**
	 * �ڴ�����
	 */
	private ServerState state = ServerState.NOTHING;
	private boolean isLogReturnSucc;//��־��׼ �Ƿ�ɹ�
	private boolean isLogError;// �Ƿ���־����
	private boolean isCmdReturnSucc;//�����׼ �Ƿ�ɹ�
	private boolean isTelnetReturnSucc;//telnet��׼ �Ƿ�ɹ�
	private CmdInfo cmd_start_up;//��������
	private CmdInfo cmd_tail_log;//��־����
	private CmdInfo cmd_shut_down;//�ط�����
	private CmdInfo cmd_telnet_freeze;//ά����������
	private CmdInfo cmd_telnet_online_players;//������������
	ExecuteCmdThread cmd_thread;//�����߳�
	
	public OnlineServerAgent(Server server) {
		super(server);
	}
	/**
	 * ִ�п�������
	 * @return
	 */
	public ExecuteResult ssh_start_up_server() {
		setState(ServerState.START_UP);
		ExecuteResult result = start_up();
		if (!isCmdReturnSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"�������Ϣ���δͨ������־�߳�ȡ������������ѯ��ط�������Ա������");
			return result;
		}
		ExecuteResult log_result = open_tail_log();
		if (!log_result.isSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"��־���Ӵ�ʧ�ܡ�����");
		};
		return result;
	}
	
	/**
	 * ִ��ͣ������
	 * @return
	 */
	public ExecuteResult ssh_shutdown() {
		//�ȱ�����Ϣ
		telnet_freeze();
		if (!isExecSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"[��������]δͨ����ͣ��������ֹ������");
			return ExecuteResult.createFailResult("ʧ��");
		}
		try {
			Thread.sleep(Const.SECOND);
		} catch (Exception e) {
			AppLog.error(server,e);
		}
		setState(ServerState.SHUT_DOWN);
		ExecuteResult result = shut_up();
		if (!isCmdReturnSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"�������Ϣ���δͨ������־�߳�ȡ������������");
			return result;
		}
		return result;
	}
	
	/**
	 * ִ��ά��������������
	 * @return
	 */
	public ExecuteResult telnet_freeze() {
		setState(ServerState.FREEZE);
		ExecuteResult result = open_tail_log();
		if (!result.isSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"��־���Ӵ�ʧ�ܡ�����");
		};
		return freeze();
		
	}
	
	/**
	 * �ر���Ϸ����
	 * @return
	 */
	public ExecuteResult shut_up() {
		try {
			//�Ƿ�����
			if(!server.getNet().isConnectCmdSSH()) {
				if (!server.getNet().openSsh_cmd_connect()) {
					SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"���� SSH CMD ����ʧ�ܡ�����");
					return ExecuteResult.createFailResult("ssh cmd ����ʧ��...");
				}
			}
			//ִ������
			ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd_shut_down);
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_shut_down.getShowName()+"����ʧ��...");
			}
			return ExecuteResult.createSuccResult("ִ��"+cmd_shut_down+"�ɹ���");
		} catch (Exception e) {
			AppLog.error(server, "shut_up is error ....", e);
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"������������Ϊʧ�ܡ�����");
			return ExecuteResult.createFailResult("ִ��"+cmd_shut_down+"ʧ��...");
		}
	}
	
	/**
	 * ����־����
	 * @return
	 */
	public ExecuteResult open_tail_log() {
		try {
			//�Ƿ�����
			if (server.getNet().isConnectCmdLog()) {
				SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"��־�߳��Ѿ������У�����");
				return ExecuteResult.createSuccResult("��־�߳̿�����");
			}
			if(!server.getNet().openSsh_log_connect()) {
				return ExecuteResult.createFailResult("ssh log ����ʧ��...");
			}
			//ִ������
			server.getNet().runTailLogThread(server);
			return ExecuteResult.createSuccResult(server.getName() + "�����߳�...");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("ִ��"+cmd_shut_down+"ʧ��...");
		}
	}
	public ExecuteResult connServer(Server conn_server) {
		if(!conn_server.getNet().openSsh_cmd_connect()) {
			SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(conn_server,"SSH����ʧ�ܡ�����");
			return ExecuteResult.RESULT_FAIL;
		}
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 * ������Ϸ����
	 * @return
	 */
	public ExecuteResult start_up() {
		try {
			//�Ƿ�����
//			if(!server.getNet().isConnectCmdSSH()) {
				if(connServer(server).isFail()) {
					return ExecuteResult.createFailResult("ssh cmd ����ʧ��...");
				}
//			}
			//ִ������
			ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd_start_up);
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"����ʧ��...");
			}
			return ExecuteResult.createSuccResult("ִ��"+cmd_start_up+"�ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("ִ��"+cmd_start_up+"ʧ��...");
		}
	}

	/**
	 * ά������������
	 * @return
	 */
	public ExecuteResult freeze() {
		try {
//			//�Ƿ�����
//			if(!server.getNet().isConnectTelnet()) {
//				if(!server.getNet().openTelnet()) {
//					return ExecuteResult.createFailResult("telnet����ʧ��...");
//				}
//			}
//			//ִ������
//			SSHMain.mainFrame.getOperate_online_server_panel().addNormalResultMsg(server,"�������ݿ�ʼ������");
//			ExecuteResult result = server.getNet().getTelnet().execCmd(cmd_telnet_freeze);
//			SSHMain.mainFrame.getOperate_online_server_panel().addNormalResultMsg(server,"�������ݽ���������");
//			if(!result.isSucc()) {
//				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"����ʧ��...");
//			}
//			result = server.getNet().getTelnet().execCmd(cmd_telnet_freeze);
//			if(!result.isSucc()) {
//				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"����ʧ��...");
//			}
//			result = server.getNet().getTelnet().execCmd(cmd_telnet_online_players);
//			if(!result.isSucc()) {
//				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"����ʧ��...");
//			}
			cmd_telnet(cmd_telnet_freeze);
			server.getNet().getTelnet().disconect();
			return ExecuteResult.createSuccResult("ִ��"+cmd_start_up+"�ɹ���");
		} catch (Exception e) {
			AppLog.error(server,"freeze is errror...",e);
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"ִ�б�������������뾡����ѯ��ط�������Ա������");
			return ExecuteResult.createFailResult("ִ��"+cmd_start_up+"ʧ��...");
		}
	}
	

	public CmdInfo getCmd_start_up() {
		return cmd_start_up;
	}

	public void setCmd_start_up(CmdInfo cmd_start_up) {
		this.cmd_start_up = cmd_start_up;
	}

	public CmdInfo getCmd_tail_log() {
		return cmd_tail_log;
	}

	public void setCmd_tail_log(CmdInfo cmd_tail_log) {
		this.cmd_tail_log = cmd_tail_log;
	}

	public CmdInfo getCmd_shut_down() {
		return cmd_shut_down;
	}

	public void setCmd_shut_down(CmdInfo cmd_shut_down) {
		this.cmd_shut_down = cmd_shut_down;
	}

	public CmdInfo getCmd_telnet_freeze() {
		return cmd_telnet_freeze;
	}

	public void setCmd_telnet_freeze(CmdInfo cmd_telnet_freeze) {
		this.cmd_telnet_freeze = cmd_telnet_freeze;
	}

	public CmdInfo getCmd_telnet_online_players() {
		return cmd_telnet_online_players;
	}

	public void setCmd_telnet_online_players(CmdInfo cmd_telnet_online_players) {
		this.cmd_telnet_online_players = cmd_telnet_online_players;
	}

	public ServerState getState() {
		return state;
	}

	public void setState(ServerState state) {
		this.state = state;
		isLogReturnSucc = false;
		isCmdReturnSucc = false;
		isTelnetReturnSucc = false;
		isLogError = false;
	}
	
	
	
	public boolean isExecSucc() {
		AppLog.info(server,"state : " + state.getName()+",isLogError :"+isLogError+",isTelnetReturnSucc:"+isTelnetReturnSucc+",isLogReturnSucc:"+isLogReturnSucc+",isCmdReturnSucc:"+isCmdReturnSucc);
		switch (state) {
		case FREEZE :
			return  !isLogError &&  isTelnetReturnSucc && isLogReturnSucc;
		default :
			return !isLogError && isLogReturnSucc && isCmdReturnSucc;
			
		}
	}
	

	public boolean isCmdReturnSucc() {
		return isCmdReturnSucc;
	}

	public void setCmdReturnSucc(boolean isCmdReturnSucc) {
		this.isCmdReturnSucc = isCmdReturnSucc;
	}

	public boolean isLogReturnSucc() {
		return isLogReturnSucc;
	}

	public void setLogReturnSucc(boolean isLogReturnSucc) {
		this.isLogReturnSucc = isLogReturnSucc;
	}

	public ExecuteCmdThread getCmd_thread() {
		return cmd_thread;
	}

	public void setCmd_thread(ExecuteCmdThread cmd_thread) {
		this.cmd_thread = cmd_thread;
	}

	
	
	public boolean isLogError() {
		return isLogError;
	}

	public void setLogError(boolean isLogError) {
		this.isLogError = isLogError;
	}

	public boolean isTelnetReturnSucc() {
		return isTelnetReturnSucc;
	}
	public void setTelnetReturnSucc(boolean isTelnetReturnSucc) {
		this.isTelnetReturnSucc = isTelnetReturnSucc;
	}

	/**
	 * ���п����߳�
	 * @return
	 */
	public ExecuteResult run_thread_cmd_start() {
		threadClose();
		threadStart(ServerState.START_UP);
		return ExecuteResult.createSuccResult("������ʼ!!!");
	}
	
	private void threadStart(ServerState execState) {
		cmd_thread = new ExecuteCmdThread(server, execState);
		cmd_thread.start();
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,execState.getName()+"�߳���������������");
	}

	/**
	 * ���йط��߳�
	 * @return
	 */
	public ExecuteResult run_thread_cmd_close() {
		threadClose();
		threadStart(ServerState.SHUT_DOWN);
		return ExecuteResult.createSuccResult("�ط���ʼ!!!");
	}
	
	/**
	 * ����ά�������߳�
	 * @return
	 */
	public ExecuteResult run_thread_cmd_freeze() {
		threadClose();
		threadStart(ServerState.FREEZE);
		return ExecuteResult.createSuccResult("ά����ʼ!!!");
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	public void threadClose() {
		try {
			if (cmd_thread == null) {
				return;
			}
			cmd_thread.interrupted();
			cmd_thread.stop();
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"ԭ"+state.getName()+"�߳�ǿ�ƹرգ�����");
		} catch (Exception e) {
			AppLog.error(server,"threadClose is error...", e);
		}
		
	}

	public CmdInfo getStateCmdInfo() {
		switch (state) {
		case START_UP :
		return cmd_start_up;
		case SHUT_DOWN :
		return cmd_shut_down;
		case FREEZE :
		return cmd_telnet_freeze;
			
		}
		return null;
	}

	public ExecuteResult ssh_upload_dirs(String final_remote_dir,UpLoadFileGroup upload_files) {
		try {
			if(!server.getNet().openSsh_cmd_connect()) {
				return ExecuteResult.createFailResult("ssh cmd ����ʧ��...");
			}
			//ִ������
			//ɾ��ԭ��������ļ�Ŀ¼
			ClientSSH cmd = server.getNet().getSsh_cmd();
			switch (server.getServerType()) {
			case MAIN_NET:
			{
				ExecuteResult result = cmd.execCmd(new CmdInfo(String.format("rm -r %s",final_remote_dir), "ɾ����������Ŀ¼������"));
				if (!result.isSucc()) {
					SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server, "�����ļ�������ʧ�ܡ�����");
					return result;
				}
				break;
			}
			}
			//�����Ǵ���������Ҫ���ļ�Ŀ¼
			for(String path : upload_files.getMk_dirs()) {
				ExecuteResult result = cmd.execCmd(new CmdInfo(String.format("mkdir -p %s%s",final_remote_dir,path), "��������Ҫ�ķ�����Ŀ¼"));
				if(!result.isSucc()) {
					return result;
				}
			}
			long time = System.currentTimeMillis();
			//�ϴ��ļ����ļ�
			ExecuteResult result = cmd.upload(final_remote_dir,upload_files.getSelect_files());
			AppLog.info(server,"����ʱ�䣺"+(System.currentTimeMillis() - time));
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"����ʧ��...");
			}
			return ExecuteResult.createSuccResult("ִ��"+cmd_start_up+"�ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("ִ��"+cmd_start_up+"ʧ��...");
		}
	}
	

		/**
		 * �������������ַ�ָ��Ŀ¼�µ�����
		 * @param path
		 * @param select_servers
		 * @return
		 */
	public ExecuteResult ssh_scp_to_other_server(List<String> from_dirs,String to_dir,Server target_server) {
		try {
			for (String from_dir : from_dirs) {
				ExecuteResult result = ssh_scp_to_other_server(from_dir,to_dir,target_server);
				if (result.isFail()) {
					return result;
				}
			}
			return ExecuteResult.createSuccResult("�ɹ�");
		} catch (Exception e) {
			AppLog.error(server, "scp from_dir "+target_server+"["+to_dir+"]  is error...", e);
			return ExecuteResult.createFailResult(String.format("scp��%s���µ�%sʧ��",server.getName(),target_server.getName()));
		}
	}
	
	public ExecuteResult ssh_scp_to_other_server(String from_dir, String to_dir,Server target_server) {
			return SshUtil.ssh_pass_to_other_server(to_dir, target_server,server, from_dir);
	}
	public ExecuteResult cmd_telnet(CmdInfo cmd) {
		if (!server.getNet().isConnectTelnet()) {
			if (!server.getNet().openTelnet()) {
				return ExecuteResult.createFailResult("telnet����ʧ��...");
			}
		}
		// ִ������
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "telnetִ��["+cmd.getShowName()+"]��ʼ��");
		ExecuteResult result = server.getNet().getTelnet().execCmd(cmd);
		if (!result.isSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server, "telnetִ��["+cmd.getShowName()+"]ʧ�ܣ�");
			return ExecuteResult.createFailResult(result.getMsg() + "/n"+ cmd_start_up.getShowName() + "����ʧ��...");
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "telnetִ��["+cmd.getShowName()+"]������");
		return ExecuteResult.createSuccResult("ִ�гɹ�!");
	
	}
	
	public ExecuteResult cmd_telnet(List<CmdInfo> list) {
		ExecuteResult result = open_tail_log();
		if (!result.isSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"��־���Ӵ�ʧ�ܡ�����");
		}
		for (CmdInfo cmd : list) {
			cmd_telnet(cmd);
		}
		server.getNet().getTelnet().disconect();
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(SshUtil.getStringEOR("mhxx@2011.new",Const.EOR_KEY));
	}

	public void exit() {
		server.getNet().close();
	}
	public void init() {
		setCmd_start_up(new CmdInfo(String.format("%s %s%s","bash",server.getSsh_remoteDir(),OnlineServerManager.CMD_START_UP.getCmd()), 
				OnlineServerManager.CMD_START_UP.getShowName(),OnlineServerManager.CMD_START_UP.getReturn_true_msg(),OnlineServerManager.CMD_START_UP.getReturn_error_msg()));
		server.getAgent_online_server().setCmd_shut_down(new CmdInfo(String.format("%s %s%s","bash",server.getSsh_remoteDir(),OnlineServerManager.CMD_SHUT_DOWN.getCmd()),
				OnlineServerManager.CMD_SHUT_DOWN.getShowName(),OnlineServerManager.CMD_SHUT_DOWN.getReturn_true_msg(),OnlineServerManager.CMD_SHUT_DOWN.getReturn_error_msg()));
		server.getAgent_online_server().setCmd_tail_log(new CmdInfo(String.format("%s %s%s","tail -f",server.getSsh_remoteDir(),OnlineServerManager.CMD_TAIL_LOG.getCmd()), 
				OnlineServerManager.CMD_TAIL_LOG.getShowName(),OnlineServerManager.CMD_TAIL_LOG.getReturn_true_msg(),OnlineServerManager.CMD_TAIL_LOG.getReturn_error_msg()));
		server.getAgent_online_server().setCmd_telnet_freeze(new CmdInfo(OnlineServerManager.CMD_TELNET_FREEZE.getCmd(), OnlineServerManager.CMD_TELNET_FREEZE.getShowName()));
		server.getAgent_online_server().setCmd_telnet_online_players(new CmdInfo(OnlineServerManager.CMD_TELNET_ONLINEPLAYERS.getCmd(), OnlineServerManager.CMD_TELNET_ONLINEPLAYERS.getShowName()));		
	}
	
}

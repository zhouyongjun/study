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
	 * 内存数据
	 */
	private ServerState state = ServerState.NOTHING;
	private boolean isLogReturnSucc;//日志标准 是否成功
	private boolean isLogError;// 是否日志报错
	private boolean isCmdReturnSucc;//命令标准 是否成功
	private boolean isTelnetReturnSucc;//telnet标准 是否成功
	private CmdInfo cmd_start_up;//开服命令
	private CmdInfo cmd_tail_log;//日志命令
	private CmdInfo cmd_shut_down;//关服命令
	private CmdInfo cmd_telnet_freeze;//维护保存命令
	private CmdInfo cmd_telnet_online_players;//在线人数命令
	ExecuteCmdThread cmd_thread;//命令线程
	
	public OnlineServerAgent(Server server) {
		super(server);
	}
	/**
	 * 执行开服命令
	 * @return
	 */
	public ExecuteResult ssh_start_up_server() {
		setState(ServerState.START_UP);
		ExecuteResult result = start_up();
		if (!isCmdReturnSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"命令反馈信息检测未通过，日志线程取消启动，请质询相关服务器人员。。。");
			return result;
		}
		ExecuteResult log_result = open_tail_log();
		if (!log_result.isSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"日志链接打开失败。。。");
		};
		return result;
	}
	
	/**
	 * 执行停服命令
	 * @return
	 */
	public ExecuteResult ssh_shutdown() {
		//先保存信息
		telnet_freeze();
		if (!isExecSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"[保存数据]未通过，停服命令终止。。。");
			return ExecuteResult.createFailResult("失败");
		}
		try {
			Thread.sleep(Const.SECOND);
		} catch (Exception e) {
			AppLog.error(server,e);
		}
		setState(ServerState.SHUT_DOWN);
		ExecuteResult result = shut_up();
		if (!isCmdReturnSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"命令反馈信息检测未通过，日志线程取消启动。。。");
			return result;
		}
		return result;
	}
	
	/**
	 * 执行维护保存数据命令
	 * @return
	 */
	public ExecuteResult telnet_freeze() {
		setState(ServerState.FREEZE);
		ExecuteResult result = open_tail_log();
		if (!result.isSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"日志链接打开失败。。。");
		};
		return freeze();
		
	}
	
	/**
	 * 关闭游戏方法
	 * @return
	 */
	public ExecuteResult shut_up() {
		try {
			//是否连接
			if(!server.getNet().isConnectCmdSSH()) {
				if (!server.getNet().openSsh_cmd_connect()) {
					SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"开启 SSH CMD 连接失败。。。");
					return ExecuteResult.createFailResult("ssh cmd 连接失败...");
				}
			}
			//执行命令
			ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd_shut_down);
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_shut_down.getShowName()+"可能失败...");
			}
			return ExecuteResult.createSuccResult("执行"+cmd_shut_down+"成功！");
		} catch (Exception e) {
			AppLog.error(server, "shut_up is error ....", e);
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"开服报错，可能为失败。。。");
			return ExecuteResult.createFailResult("执行"+cmd_shut_down+"失败...");
		}
	}
	
	/**
	 * 打开日志方法
	 * @return
	 */
	public ExecuteResult open_tail_log() {
		try {
			//是否连接
			if (server.getNet().isConnectCmdLog()) {
				SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"日志线程已经开启中！！！");
				return ExecuteResult.createSuccResult("日志线程开启中");
			}
			if(!server.getNet().openSsh_log_connect()) {
				return ExecuteResult.createFailResult("ssh log 连接失败...");
			}
			//执行命令
			server.getNet().runTailLogThread(server);
			return ExecuteResult.createSuccResult(server.getName() + "开启线程...");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("执行"+cmd_shut_down+"失败...");
		}
	}
	public ExecuteResult connServer(Server conn_server) {
		if(!conn_server.getNet().openSsh_cmd_connect()) {
			SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(conn_server,"SSH链接失败。。。");
			return ExecuteResult.RESULT_FAIL;
		}
		return ExecuteResult.RESULT_SUCC;
	}
	/**
	 * 启动游戏方法
	 * @return
	 */
	public ExecuteResult start_up() {
		try {
			//是否连接
//			if(!server.getNet().isConnectCmdSSH()) {
				if(connServer(server).isFail()) {
					return ExecuteResult.createFailResult("ssh cmd 连接失败...");
				}
//			}
			//执行命令
			ExecuteResult result = server.getNet().getSsh_cmd().execCmd(cmd_start_up);
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"可能失败...");
			}
			return ExecuteResult.createSuccResult("执行"+cmd_start_up+"成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("执行"+cmd_start_up+"失败...");
		}
	}

	/**
	 * 维护服务器操作
	 * @return
	 */
	public ExecuteResult freeze() {
		try {
//			//是否连接
//			if(!server.getNet().isConnectTelnet()) {
//				if(!server.getNet().openTelnet()) {
//					return ExecuteResult.createFailResult("telnet链接失败...");
//				}
//			}
//			//执行命令
//			SSHMain.mainFrame.getOperate_online_server_panel().addNormalResultMsg(server,"保存数据开始！！！");
//			ExecuteResult result = server.getNet().getTelnet().execCmd(cmd_telnet_freeze);
//			SSHMain.mainFrame.getOperate_online_server_panel().addNormalResultMsg(server,"保存数据结束！！！");
//			if(!result.isSucc()) {
//				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"可能失败...");
//			}
//			result = server.getNet().getTelnet().execCmd(cmd_telnet_freeze);
//			if(!result.isSucc()) {
//				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"可能失败...");
//			}
//			result = server.getNet().getTelnet().execCmd(cmd_telnet_online_players);
//			if(!result.isSucc()) {
//				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"可能失败...");
//			}
			cmd_telnet(cmd_telnet_freeze);
			server.getNet().getTelnet().disconect();
			return ExecuteResult.createSuccResult("执行"+cmd_start_up+"成功！");
		} catch (Exception e) {
			AppLog.error(server,"freeze is errror...",e);
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"执行保存数据命令报错，请尽快质询相关服务器人员。。。");
			return ExecuteResult.createFailResult("执行"+cmd_start_up+"失败...");
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
	 * 运行开服线程
	 * @return
	 */
	public ExecuteResult run_thread_cmd_start() {
		threadClose();
		threadStart(ServerState.START_UP);
		return ExecuteResult.createSuccResult("开服开始!!!");
	}
	
	private void threadStart(ServerState execState) {
		cmd_thread = new ExecuteCmdThread(server, execState);
		cmd_thread.start();
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,execState.getName()+"线程正常启动！！！");
	}

	/**
	 * 运行关服线程
	 * @return
	 */
	public ExecuteResult run_thread_cmd_close() {
		threadClose();
		threadStart(ServerState.SHUT_DOWN);
		return ExecuteResult.createSuccResult("关服开始!!!");
	}
	
	/**
	 * 运行维护数据线程
	 * @return
	 */
	public ExecuteResult run_thread_cmd_freeze() {
		threadClose();
		threadStart(ServerState.FREEZE);
		return ExecuteResult.createSuccResult("维护开始!!!");
	}
	
	@SuppressWarnings({ "static-access", "deprecation" })
	public void threadClose() {
		try {
			if (cmd_thread == null) {
				return;
			}
			cmd_thread.interrupted();
			cmd_thread.stop();
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"原"+state.getName()+"线程强制关闭！！！");
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
				return ExecuteResult.createFailResult("ssh cmd 连接失败...");
			}
			//执行命令
			//删除原来的相关文件目录
			ClientSSH cmd = server.getNet().getSsh_cmd();
			switch (server.getServerType()) {
			case MAIN_NET:
			{
				ExecuteResult result = cmd.execCmd(new CmdInfo(String.format("rm -r %s",final_remote_dir), "删除主服更新目录下内容"));
				if (!result.isSucc()) {
					SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server, "更新文件到主服失败。。。");
					return result;
				}
				break;
			}
			}
			//首先是创建所有需要的文件目录
			for(String path : upload_files.getMk_dirs()) {
				ExecuteResult result = cmd.execCmd(new CmdInfo(String.format("mkdir -p %s%s",final_remote_dir,path), "创建所需要的服务器目录"));
				if(!result.isSucc()) {
					return result;
				}
			}
			long time = System.currentTimeMillis();
			//上传文件组文件
			ExecuteResult result = cmd.upload(final_remote_dir,upload_files.getSelect_files());
			AppLog.info(server,"花费时间："+(System.currentTimeMillis() - time));
			if(!result.isSucc()) {
				return ExecuteResult.createFailResult(result.getMsg()+"/n"+cmd_start_up.getShowName()+"可能失败...");
			}
			return ExecuteResult.createSuccResult("执行"+cmd_start_up+"成功！");
		} catch (Exception e) {
			e.printStackTrace();
			return ExecuteResult.createFailResult("执行"+cmd_start_up+"失败...");
		}
	}
	

		/**
		 * 向其他服务器分发指定目录下的数据
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
			return ExecuteResult.createSuccResult("成功");
		} catch (Exception e) {
			AppLog.error(server, "scp from_dir "+target_server+"["+to_dir+"]  is error...", e);
			return ExecuteResult.createFailResult(String.format("scp从%s更新到%s失败",server.getName(),target_server.getName()));
		}
	}
	
	public ExecuteResult ssh_scp_to_other_server(String from_dir, String to_dir,Server target_server) {
			return SshUtil.ssh_pass_to_other_server(to_dir, target_server,server, from_dir);
	}
	public ExecuteResult cmd_telnet(CmdInfo cmd) {
		if (!server.getNet().isConnectTelnet()) {
			if (!server.getNet().openTelnet()) {
				return ExecuteResult.createFailResult("telnet链接失败...");
			}
		}
		// 执行命令
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "telnet执行["+cmd.getShowName()+"]开始！");
		ExecuteResult result = server.getNet().getTelnet().execCmd(cmd);
		if (!result.isSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server, "telnet执行["+cmd.getShowName()+"]失败！");
			return ExecuteResult.createFailResult(result.getMsg() + "/n"+ cmd_start_up.getShowName() + "可能失败...");
		}
		SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "telnet执行["+cmd.getShowName()+"]结束！");
		return ExecuteResult.createSuccResult("执行成功!");
	
	}
	
	public ExecuteResult cmd_telnet(List<CmdInfo> list) {
		ExecuteResult result = open_tail_log();
		if (!result.isSucc()) {
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"日志链接打开失败。。。");
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

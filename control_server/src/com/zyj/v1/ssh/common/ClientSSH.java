package com.zyj.v1.ssh.common;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Element;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import com.zyj.v1.SSHMain;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.log_control.LogControl;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.online_server.ServerState;
import com.zyj.v1.ssh.online_server.UpLoadFile;
import com.zyj.v1.ssh.util.Const;
	/**
	 * 本地SHH客户端链接信息
	 * @author zhouyongjun
	 *
	 */
public class ClientSSH {
	private Server server;
	private Connection conn;//链接
	private SCPClient scp;//scp转送
	private SFTPv3Client ftp;
	private Session session;//session
	boolean isLogSsh;//是否日志类型
	private long delayTime;//是否日志类型
	Map<ServerState,List<LogControl>> log_states;
	public ClientSSH(Server server,boolean isLogSsh) {
		this.server = server;
		this.isLogSsh = isLogSsh;
	}
	
	public Connection getConn() {
		return conn;
	}
	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
	public void load(Element e) {
		
	}

	public Connection openConnection() {
		try {
			String host = server.getSsh_host();
			int port = server.getSsh_port();
			String username = server.getSsh_username();
			String password = server.getSsh_password();
			AppLog.info(server,"connecting to " + server.getSsh_host() + " with user " + server.getSsh_username()+ " and pwd " + server.getSsh_password());
			conn = new Connection(host,port);
			conn.connect(); // make sure the connection is opened
			boolean isAuthenticated = conn.authenticateWithPassword(username,password);
			if (isAuthenticated == false) {
				return null;
			}
			//如果是日志类型，则要初始化下日志类型
			if (isLogSsh) {
				initLogControls();
			}else {
				scp = new SCPClient(conn);
				ftp = new SFTPv3Client(conn);
			}
			return conn;
		} catch (Exception e) {
			AppLog.error(server, toString() +" openConnection is error...", e);
			return null;
		}
	}
	
	/**
	 * 初始化下日志监控数据
	 */
	private void initLogControls() {
		log_states = new HashMap<ServerState,List<LogControl>>();
		for (Entry<ServerState,List<CmdInfo>> entry : OnlineServerManager.LOG_CONFIG_INFOS.entrySet()) {
			List<LogControl> temp_list = new ArrayList<LogControl>();
			log_states.put(entry.getKey(), temp_list);
			for (CmdInfo info : entry.getValue()) {
				temp_list.add(new LogControl(info.getCmd(),info.getShowName()));
			}
		}
	}
	
	/**
	 * 关闭链接
	 * @return
	 */
	public int closeConnection(){
		int status = 0;
		try {
			AppLog.info(server,"close connecting to " + this);
			if (session != null) {
				session.close();				
			}
			if (conn != null) {
				conn.close();	
			}
			
			session = null;
			conn = null;
		} catch (Exception e) {
			AppLog.error(server, toString() +" closeConnection is error...", e);
		}
		return status;
	}
	

	/**
	 * 执行命令
	 * @param cmd
	 * @return
	 */
	public ExecuteResult execCmd(CmdInfo cmd) {
		try {
			session = conn.openSession();
			ExecuteResult result = sendCmdToServer(cmd);
			if (!result.isSucc()) {
				return result;
			}
		    return receiverMsgFromServer(cmd); 
		} catch (Exception e) {
			AppLog.error(server,toString()+"execCmd["+cmd+"] is error...",e);
			if(SSHMain.mainFrame != null) SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"执行"+cmd.getShowName()+"命令报错，可能失败，请质询相关服务器人员。。。");
			return ExecuteResult.createFailResult("执行"+cmd+"报错...");
		}
	}

	/**
	 * 接受信息
	 * @param showCmdName
	 * @return
	 */
	private ExecuteResult receiverMsgFromServer(CmdInfo cmd) {
		InputStream stdout = null;
		BufferedReader br = null;
		try {
			stdout = new StreamGobbler(session.getStdout());
			 br = new BufferedReader(new InputStreamReader(stdout,"UTF-8"));
			 AppLog.info(server,"shh cmd result:");
			 String line = null;
		    while ((line = br.readLine()) != null) {
		      AppLog.info(server,line);
		      if (SSHMain.mainFrame != null) {
		    	  switch (SSHMain.mainFrame.getPanel_operate_online_server().getOperateType()) {
			      case SERVER_MAINTAIN:
			      {
			    	  if (isLogSsh) {
			    		  if (cmd.getCmd().startsWith("date +%")) {
			    			  return ExecuteResult.createSuccResult(line);
			    		  }else {
					    	  if (delayTime > 0  && delayTime < System.currentTimeMillis()) {
					    		  AppLog.info(server,toString() + "shh receiverMsgFromServer["+cmd+"] delayTime["+new Date(delayTime)+"] log control is close....");
					    		  SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,"日志线程到时强制关闭！！！");
					    		  return ExecuteResult.createSuccResult("接受关闭日志打印信息!");
					    	  }
					    	  checkLogControl(cmd,line);		    			  
			    		  }
				      }else {
				    	  checkCmdControl(cmd,line);
				      }
			    	  break;
			      }
			      case UPLOAD:
			      case DOWNLOAD:
			      {
			    	  break;
			      }
			      }
			    } 
		      }
		     
		    AppLog.info(server,toString() + "receiverMsgFromServer["+cmd+"] is end....");
		    return ExecuteResult.createSuccResult("接受["+cmd+"]命令信息成功!");
		} catch (Exception e) {
			AppLog.error(server,toString()+"receiverMsgFromServer["+cmd+"] is error...",e);
			if (SSHMain.mainFrame != null) SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"接受服务器信息信息报错，可能"+cmd.getShowName()+"失败，请质询相关服务器人员。。。");
			return ExecuteResult.createSuccResult("接受["+cmd+"]命令信息报错...");
		}finally {
			try {
				br.close();
				stdout.close();
			} catch (IOException e) {
				AppLog.error(server,e);
			}
		}
		
		
	}
	
	public void checkCmdControl(CmdInfo cmd,String line) {
		CmdInfo info = server.getAgent_online_server().getStateCmdInfo();
		if (info == null) {
			return;
		}
		if (info.getReturn_true_msg() != null && line.contains(info.getReturn_true_msg())) {
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,cmd.getShowName()+" 命令反馈信息标准通过！！！");
			server.getAgent_online_server().setCmdReturnSucc(true);
		}
		if (info.getReturn_error_msg() != null && line.contains(info.getReturn_error_msg())) {
			server.getAgent_online_server().setCmdReturnSucc(false);
			String msg = null;
			if (server.getAgent_online_server().getState() == ServerState.START_UP) {
				msg ="执行"+cmd.getShowName()+"命令失败，反馈信息:服务器已开启。。。";
			}else {
				msg ="执行"+cmd.getShowName()+"命令失败，反馈信息:服务器已关闭。。。";
			}
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,msg);
		}
	}

	/**
	 * 检测日志监控条件
	 * @param line
	 */
	private void checkLogControl(CmdInfo cmd,String line) {
		String lowner_line = line.toLowerCase();
		if (lowner_line.contains("error") || lowner_line.contains("exception")) {
			AppLog.error(server,toString() + cmd + " 检测到有报错["+line+"]");
			server.getAgent_online_server().setLogError(true);
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"执行"+cmd.getShowName()+"命令,检测日志有报错，请质询相关服务器人员。。。");
			return;
		}
		if (!log_states.containsKey(server.getAgent_online_server().getState())) {
			return;
		}
		List<LogControl> list = log_states.get(server.getAgent_online_server().getState());
		//如果没有条件或者已经执行成功 则不需要在检测条件了
		if (list == null || server.getAgent_online_server().isLogReturnSucc()) {
			return;
		}
		//检测条件
		for (LogControl control : list) {
			//满足的情况下
			if (lowner_line.contains(control.getControl_msg())) {
				control.setThrougth(true);
				AppLog.info(server,toString() + " 检测"+control.getControl_msg()+"条件 通过!");
				//检测是否全部通过
				checkAllLogControlThrough(cmd);
				break;
			}
		}
	}
	
	/**
	 * 检测是否所有条件通过
	 */
	private void checkAllLogControlThrough(CmdInfo cmd) {
			for (LogControl control : log_states.get(server.getAgent_online_server().getState())) {
				//若不满足则跳出该方法
				if (!control.isThrougth()) {
					return;
				}
			}
			server.getAgent_online_server().setLogReturnSucc(true);
			delayTime = System.currentTimeMillis() + 30 * Const.MINUTE;
			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server,server.getAgent_online_server().getState().getName()+" 日志检测通过!!!");
			if (server.getAgent_online_server().isExecSucc()) {
				SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(server,"***["+server.getAgent_online_server().getState().getName()+"]执行成功***");
			}
	}

	/**
	 * 向服务器发送命令
	 * @param cmd
	 * @return
	 */
	private ExecuteResult sendCmdToServer(CmdInfo cmd) {
		AppLog.info(server,toString()+ "shh cmd exec:"+cmd);
		try {
			session.execCommand(cmd.getCmd());
			return ExecuteResult.createSuccResult("发送"+cmd+"成功！");
		} catch (Exception e) {
			AppLog.error(server,toString()+"sendCmdToServer["+cmd+"] is error...",e);
			SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server,"向服务器发送信息报错，执行["+cmd.getShowName()+"，请尽快质询相关服务器人员。。。");
		}
		return ExecuteResult.createFailResult("发送"+cmd+"报错...");
	}

	public boolean isConnect() {
		return conn != null && conn.isAuthenticationComplete() && session != null ;
	}
	
	public String toString() {
		return " SSH[isLog:"+isLogSsh+",hascode:"+hashCode()+"] ";
	}
	
	public ExecuteResult scpLocalToServer(String localDir,String remoteDir) {
		try {
			scp.put(new String[]{"./up/cs_zyj/joy_test.jar"}, server.getSsh_remoteDir()+ "cs_zyj/");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public SCPClient getScp() {
		return scp;
	}

	public void setScp(SCPClient scp) {
		this.scp = scp;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public boolean isLogSsh() {
		return isLogSsh;
	}

	public void setLogSsh(boolean isLogSsh) {
		this.isLogSsh = isLogSsh;
	}

	public long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

	public Map<ServerState, List<LogControl>> getLog_states() {
		return log_states;
	}

	public void setLog_states(Map<ServerState, List<LogControl>> log_states) {
		this.log_states = log_states;
	}

	public SFTPv3Client getFtp() {
		return ftp;
	}

	public void setFtp(SFTPv3Client ftp) {
		this.ftp = ftp;
	}
	
	/**
	 * 向服务器上传文件
	 * @param remote_dir 服务器目标 目录下
	 * @param upLoadFile 本地上传文件信息
	 * @return
	 */
	public ExecuteResult upload(String remote_dir,UpLoadFile upLoadFile) {
		String final_remote_dir = remote_dir+upLoadFile.getPath();
		String load_file = upLoadFile.getFile().getPath();
		return upload(final_remote_dir, load_file);
	}
	
	/**
	 * 向服务器上传文件
	 * @param remote_dir 服务器目标 目录下
	 * @param upLoadFile 本地上传文件信息
	 * @return
	 */
	public ExecuteResult upload(String final_remote_dir,String load_file) {
		AppLog.info(server,"upload load_file["+load_file+"] to server remote_dir["+final_remote_dir+"]...");
		try {
			scp.put(load_file,final_remote_dir);
			return ExecuteResult.createSuccResult("更新"+load_file+"成功");
		} catch (IOException e) {
			AppLog.error(server,"upload load_file["+load_file+"] to server remote_dir["+final_remote_dir+"] is error...",e);
			return ExecuteResult.createFailResult("更新"+load_file+"失败");
		}
	}
		
	/**
	 * 向服务器上传一组文件
	 * @param remote_dir 服务器目标 目录下
	 * @param upload_files 本地一组上传文件信息
	 * @return
	 */
	public ExecuteResult upload(String remote_dir,List<UpLoadFile> upload_files) {
		for (UpLoadFile file : upload_files) {
			ExecuteResult result = upload(remote_dir, file);
			if (!result.isSucc()) {
				return result;
			}
		}
		return ExecuteResult.createSuccResult("更新成功");
	}
	/**
	 * 从服务器下载资源
	 * @param remoteFiles
	 * @return
	 */
	public ExecuteResult download(String[] remoteFiles,String local_download_dir) {
		File dir = new File(local_download_dir);
		dir.deleteOnExit();
		dir.mkdirs();
		try {
			scp.get(remoteFiles,local_download_dir);
			return ExecuteResult.RESULT_SUCC;
		} catch (IOException e) {
			AppLog.error(server,e);
			return ExecuteResult.createFailResult("下载文件失败!"); 
		}
	}
	
	public static void main(String[] args) {
		try {
			Runtime.getRuntime().exec("rar -1 .\\load.zip  .\\upload\\");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}

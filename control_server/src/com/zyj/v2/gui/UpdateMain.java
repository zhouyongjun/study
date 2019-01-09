package com.zyj.v2.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.zyj.v1.common.Instances;
import com.zyj.v1.log.AppLog;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.common.ExecuteResult;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.online_server.UpLoadFile;
import com.zyj.v1.ssh.online_server.UpLoadFileGroup;
import com.zyj.v1.ssh.util.SshUtil;

public class UpdateMain implements Instances{
	static UpdateFrame frame;
	public static String HELP_MSG;
	public static void main(String[] args) {
		init();
		SshUtil.loadResourceMapping();
		frame = new UpdateFrame();
		frame.setVisible(true);
	}
	public static void init() {
		dbMgr.init();
		onsMgr.init();
		nsMgr.init();
		loadHelp();
	}
	private static void loadHelp() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("./help.txt"), "utf-8"));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			HELP_MSG = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (br != null)try {br.close();} catch (IOException e) {}
		}
	}
	public static void updateOfMapping(boolean isUnzip,List<Server> servers) {
		//初始化服务器
		//初始化更新文件 
		UpLoadFileGroup file_group = new UpLoadFileGroup();
		File[] select_files = new File(OnlineServerManager.DIR_UPLOAD_UPDATE).listFiles();
		if (!file_group.setNeedMappingFiles(OnlineServerManager.APP_JAR,null,null,select_files)) ;
		//copy
		file_group.getSelect_files().add(new UpLoadFile("", new File("./lib/zip.exe")));
		SshUtil.copyFiles(null,null,file_group,OnlineServerManager.DIR_UPLOAD_ZIP);
		updateToServers(isUnzip, servers);
	}
	
	public static void updateOfxCopy(boolean isUnzip,List<Server> servers) {
		SshUtil.copyFiles(null,null,OnlineServerManager.DIR_UPLOAD_UPDATE,OnlineServerManager.DIR_UPLOAD_ZIP);
		SshUtil.copyFile(new UpLoadFile("", new File("./lib/zip.exe")), OnlineServerManager.DIR_UPLOAD_ZIP);
		updateToServers(isUnzip, servers);
	}
	
	public static void updateToServers(boolean isUnzip,List<Server> servers) {
		String zip_file_name = OnlineServerManager.UNIFIED_SERVER_PATH+".zip";
		//zip
		ExecuteResult result = SshUtil.zip(null,zip_file_name,OnlineServerManager.DIR_UPLOAD_ZIP);
		UpLoadFile zipFile = null;
		if (result.isSucc()) zipFile = (UpLoadFile)result.getObjs()[0];
		else {
			JOptionPane.showMessageDialog(frame, "zip失败，可查看控制台打印信息");
			return;
		}
		Server transfer_server = servers.get(0);
		String dir = getTranParentDir(transfer_server.getSsh_remoteDir());
		result = upLoadToTransferServer(transfer_server,dir,zipFile,false);
		if (result.isFail()) {
			JOptionPane.showMessageDialog(frame, "中转服务器上传失败，可查看控制台打印信息","资源上传",JOptionPane.ERROR_MESSAGE);
			return;
		}
		scpServersFromTransfer(zipFile,transfer_server,dir,servers,isUnzip);
		//服务器更新
		JOptionPane.showMessageDialog(frame, "更新完毕，可查看控制台打印信息");
		//修改名字
		File file = new File(OnlineServerManager.DIR_UPLOAD_ZIP+zip_file_name);
		File change_file = new File("old\\"+file.getName()+"."+System.currentTimeMillis());
		File parent = change_file.getParentFile();
		if (!parent.exists()) {
			parent.mkdirs();
		}
		if (file.renameTo(change_file)) {
			AppLog.info(change_file.getAbsolutePath() + " 本地保存成功!");
		}else {
			AppLog.info(change_file.getAbsolutePath() + " 本地保存失败!");
		}
	}
	
	public static String getLocalIPForJava(){
	    StringBuilder sb = new StringBuilder();
	    try {
	    	Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); 
	        while (en.hasMoreElements()) {
	            NetworkInterface intf = (NetworkInterface) en.nextElement();
	            Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
	            while (enumIpAddr.hasMoreElements()) {
	                 InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
	                 if (!inetAddress.isLoopbackAddress()  && !inetAddress.isLinkLocalAddress() 
	                		 	&& inetAddress.isSiteLocalAddress()) {
	                	 sb.append(inetAddress.getHostAddress().toString()+",");
	                 }
	             }
	          }
	        if(sb.length() > 0) sb.deleteCharAt(sb.length()-1);
	    } catch (SocketException e) {  }
	    return sb.toString();
	}
	
	private static String getTranParentDir(String ssh_remoteDir) {
		//向主服上传文件
		String[] spit_dir = ssh_remoteDir.split("/");
		StringBuffer dir = new StringBuffer();
		for (int i=0;i<spit_dir.length - 1;++i) {
			dir.append(spit_dir[i]).append("/");
		}
		dir.append("update/");
		return dir.toString();
	}
	private static void scpServersFromTransfer(UpLoadFile zipFile,Server transfer_server,String transfer_dir,
			List<Server> select_servers,boolean isUnzip) {
		long main_time = System.currentTimeMillis();
		//执行主服向各个服务器分发文件
		for (Server server : select_servers) {
//			SSHMain.mainFrame.getPanel_operate_online_server().addNormalResultMsg(server, "更新资源开始！");
//			time = System.currentTimeMillis();
			long before = System.currentTimeMillis();
			String transfer_zip_file = transfer_dir+zipFile.getFile().getName();
			String to_dir = server.getSsh_remoteDir();
			ExecuteResult scp_result = SshUtil.ssh_pass_to_other_server(to_dir,server,transfer_server,transfer_zip_file);
			if (!scp_result.isSucc()) {
//				SSHMain.mainFrame.getPanel_operate_online_server().addErrorResultMsg(server, "更新资源失败，报错！！");
				AppLog.info(server,"sshpass is error...");
				continue;
			}
			//复制一份到old目录下
			copyTransferZipFileToOldDirForTargetServer(server,to_dir,zipFile.getFile().getName());
			if (isUnzip) {
				ExecuteResult unzip_result = SshUtil.ssh_unzipfile(server, server.getSsh_remoteDir(), zipFile, null);
				if (unzip_result.isFail()) {
					AppLog.info(server,"unzip is error...");
					continue;
				}	
			}
			
			AppLog.info(server,"scp分发花费时间："+(System.currentTimeMillis() - before));
//			SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(server, "更新资源成功，耗时："+((System.currentTimeMillis() - time)/1000.0d)+"秒！！");
		}
		AppLog.info(null,"总计分发花费时间："+(System.currentTimeMillis() - main_time));
//		SSHMain.mainFrame.getPanel_operate_online_server().addSuccResultMsg(mainServer, "更新所有服务器结束，耗时："+((System.currentTimeMillis() - main_time)/1000.0d)+"秒！！");
	}
	
	private static ExecuteResult copyTransferZipFileToOldDirForTargetServer(
			Server server, String to_dir, String transfer_zip_file) {
		try {
			Properties properties = System.getProperties();
			String copy_name = transfer_zip_file 
					+"."+properties.getProperty("user.name")
					+"."+getLocalIPForJava()
					+"."+System.currentTimeMillis();
			SshUtil.ssh_cmd(server, new CmdInfo("cd " + to_dir+" && mkdir old", "创建old"));
			return SshUtil.ssh_cmd(server, new CmdInfo("cd " + to_dir+" && cp "+transfer_zip_file+" old/"+copy_name, "备份zip文件倒old目录下"));
		} catch (Exception e) {
			AppLog.error(server, e);
			return ExecuteResult.createFailResult("备份old文件错误"); 
		}
	}
	public static ExecuteResult upLoadToTransferServer(Server transferServer,String transfer_dir,UpLoadFile zipFile,boolean isUnzip) {
		ExecuteResult result = SshUtil.ssh_upload_zipfile_and_unzipfile(transferServer,transfer_dir,zipFile,null,isUnzip);
		return result;
	}
	
	/**
	 * 维护服务器
	 * @param servers
	 */
	public static void freezeServers(List<Server> servers) {
		int size = servers.size();
		int i=0;
		for (Server server : servers) {
			i++;
			if (i < size) {
				new Thread(new FreezeRunnable(server)).start();
			}else {
				ExecuteResult result = SshUtil.telnet_freeze(server);
				if (result.isFail()) {
					JOptionPane.showMessageDialog(frame,server.getName()+" " + result.getMsg(),"freeze" ,JOptionPane.ERROR_MESSAGE);
				}
				AppLog.info(server,result.getMsg());
			}
		}
		JOptionPane.showMessageDialog(frame, "freeze已执行，可查看控制台打印信息");
	}
	
 static class FreezeRunnable implements Runnable {
			Server server;
			public FreezeRunnable(Server server) {
				this.server = server;
			}
			public void run() {
				ExecuteResult result = SshUtil.telnet_freeze(server);
				AppLog.info(server,result.getMsg());
			}
		}
	
  static class StopRunnable implements Runnable {
		Server server;
		public StopRunnable(Server server) {
			this.server = server;
		}
		public void run() {
			ExecuteResult result = SshUtil.shut_up(server);
			AppLog.info(server,result.getMsg());
		}
	}
	

	public static void stopServers(List<Server> servers) {
		int size = servers.size();
		int i=0;
		for (Server server : servers) {
			i++;
			if (i < size) {
				new Thread(new StopRunnable(server)).start();
			}else {
				ExecuteResult result = SshUtil.shut_up(server);
				if (result.isFail())JOptionPane.showMessageDialog(frame, result.getMsg());
				AppLog.info(server,result.getMsg());
			}
		}
		JOptionPane.showMessageDialog(frame, "停服执行，可查看控制台打印信息");
	}
	public static void startServers(List<Server> servers) {
		for (Server server : servers) {
			ExecuteResult result = SshUtil.start_up(server);
			AppLog.info(server,result.getMsg());
		}
		JOptionPane.showMessageDialog(frame, "开服完毕，可查看控制台打印信息");
	}
}

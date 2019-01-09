package com.zyj.v1;

import java.io.IOException;
import java.util.List;

import com.zyj.v1.gui.LoginFrame;
import com.zyj.v1.gui.MainFrame;
import com.zyj.v1.ssh.Server;
import com.zyj.v1.ssh.common.ClientSSH;
import com.zyj.v1.ssh.common.ClientUser;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.db.DBManager;
import com.zyj.v1.ssh.online_server.OnlineServerManager;

	/**
	 * 主线程类
	 * @author zhouyongjun
	 *
	 */

public class SSHMain {
	public static MainFrame mainFrame;
	public static ClientUser user;
	public static void main(String[] args) {
		start();
	}
	
	/**
	 * 开始方法
	 */
	public static void start() {
		DBManager.getInstance().init();
		new LoginFrame().setVisible(true);
//		SSHManager.geteInstance().init();
//		NewServerManager.getInstance().update();
//		test();
//		testzip();
	}

	private static void testzip() {
		try {
			Process p=Runtime.getRuntime().exec("cmd /c copy E:\\3.7_JavaWorkspace\\control_server\\upload\\gameData.xml E:\\3.7_JavaWorkspace\\control_server\\update\\gameData.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void test() {
		DBManager.getInstance().init();
		OnlineServerManager.getInstance().init();
//		SSHManager.getInstance().execute_group_start_cmd(SSHManager.getInstance().getGroupsList().get(0));
		Server server = OnlineServerManager.getInstance().getGroupsList().get(0).getServers().get(1);
		server.getNet().openSsh_cmd_connect();
//		SFTPv3Client ftp = server.getNet().getSsh_cmd().getFtp();
//		try {
//			Vector<SFTPv3DirectoryEntry> vector = ftp.ls("hbsg");
//			for (int i=0;i<vector.size();i++) {
//				SFTPv3DirectoryEntry entry = vector.get(i);
//				SFTPv3FileAttributes attr = entry.attributes;
//				System.out.println(entry + " : "  +entry.filename+","+entry.longEntry+","+entry.attributes.getOctalPermissions());
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		ClientSSH ssh = server.getNet().getSsh_cmd();
//		ssh.execCmd(new CmdInfo("mkdir -p /opt/data/admin/hbsg/2_server/22/33/44", "测试"));
		ssh.execCmd(new CmdInfo("rm -r /opt/data/admin/hbsg/2_server/22", "测试"));
//		server.getNet().getSsh_cmd().scpLocalToServer(null, null);
//		server.getNet().getSsh_cmd().execCmd(new CmdInfo("sshpass -p 'joy(*&00' scp -P 22022 -o StrictHostKeyChecking=no -r  /data/admin/send/ admin@192.168.8.40:/data/admin/hbsg/"/*+server.getSsh_remoteDir()*/, "拷贝"));
//		SSHManager.getInstance().execute_group_stop_cmd(SSHManager.getInstance().getGroupsList().get(0));
	}

}

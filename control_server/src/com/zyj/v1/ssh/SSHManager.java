package com.zyj.v1.ssh;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zyj.v1.SSHMain;
import com.zyj.v1.common.Instances;
import com.zyj.v1.ssh.common.ClientSSH;
import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.common.ExecuteResult;
import com.zyj.v1.ssh.online_server.OnlineServerManager;
import com.zyj.v1.ssh.open_new_server.NewServerManager;

public class SSHManager implements Instances{
	private static SSHManager instance = new SSHManager();
	private SSHManager() {
		
	}
	public static SSHManager geteInstance() {
		return instance;
	}
	
	public void init() {
		OnlineServerManager.getInstance().init();
		NewServerManager.getInstance().init();
	}
	
	public static void main(String[] args){
//		String str="game_url=jdbc:mysql://10.80.1.247/hengban_gm?useUnicode=true&characterEncoding=utf8";
//		str=str.replaceAll("(game_url=jdbc:mysql://)(.+?)(/)", "$1localhost$3");
//		str=str.replaceAll("(.+?/)(.+?/)(.+?)(\\?)", "$1$2hbsg$4");
//		System.out.println(str);
//		String str="SERVER_NAME = test";
//		str=str.replaceAll("(SERVER_NAME)(\\s*)(=)(\\s*).*","$1$2$3$4测试");
//		System.out.println(str);
//		<config name="conf/jdbc.properties">
//		<update name="JDBC" 		param="DB_NAME:SQL_PORT"			pattern="(jdbc:mysql://)(.+?)(/)" />
//		<update name="JDBC" 		param="SQL_USERNAME" 				pattern="(game_user)(\\s*)(=)(\\s*).*" />
//		<update name="JDBC" 		param="SQL_PASSWORD" 				pattern="(game_password)(\\s*)(=)(\\s*).*" />
//		</config>
		

//game_url=jdbc:mysql://10.80.1.247/hengban_gm?useUnicode=true&characterEncoding=utf8
//game_user=root
//game_password=letang.
		String str="mysql -h127.0.0.1  -P3307 -uroot -pmhxx@2011.new";
		str=str.replaceAll("(mysql\\s+-h127.0.0.1\\s+-P).+(.*?)", "$13308 -uzyj -pzyj.123");
		System.out.println(str);
}  
	
	public ExecuteResult connServer(Server conn_server) {
		if(!conn_server.getNet().isConnectCmdSSH()) {
			if (!conn_server.getNet().openSsh_cmd_connect()) {
				return ExecuteResult.RESULT_FAIL;
			}
		}
		return ExecuteResult.RESULT_SUCC;
	}
	
	
	public ExecuteResult downloadFromServer(Server server,List<String> update_remotedir_fileslist) {
		return server.getNet().getSsh_cmd().download(update_remotedir_fileslist.toArray(new String[update_remotedir_fileslist.size()]),OnlineServerManager.DIR_DOWNLOAD);
	}
	public ExecuteResult uploadToServer(Server server,
			Map<String, String> uploadMaps) {
		for (Entry<String, String> entry : uploadMaps.entrySet()) {
			if (server.getNet().getSsh_cmd().upload(entry.getValue(),entry.getKey()).isFail()) {
				if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addErrorResultMsg(server, "   上传本地文件["+entry.getKey()+"]到服务器["+entry.getValue()+"]失败！");
				return ExecuteResult.RESULT_FAIL;
			}
			if (SSHMain.mainFrame !=null)SSHMain.mainFrame.getPanel_open_new_server().addNormalResultMsg(server, "   上传本地文件["+entry.getKey()+"]到服务器["+entry.getValue()+"]OK！");
		}
		return ExecuteResult.RESULT_SUCC;
	}
}

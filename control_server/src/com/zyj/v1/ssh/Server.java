package com.zyj.v1.ssh;

import java.util.Map;
import java.util.Scanner;

import com.zyj.v1.ssh.common.ClientNet;
import com.zyj.v1.ssh.db.DaoData;
import com.zyj.v1.ssh.online_server.OnlineServerAgent;
import com.zyj.v1.ssh.online_server.ServerType;
import com.zyj.v1.ssh.open_new_server.NewServerAgent;
import com.zyj.v1.ssh.util.Const;
import com.zyj.v1.ssh.util.SshUtil;

public class Server implements DaoData{
	private int id;//服务器id
	private String name;// 服务器名称
	private int groupId;//组ID
	private String groupName;//组名字
	private String ssh_host;//地址
	private int ssh_port;//端口号
	private String ssh_username;//用户名
	private String ssh_password;//密码
	private String ssh_remoteDir;//远程上传下载目录
	private String ssh_localDir;//本地下载目录
	private String telnet_host;//telnet地址
	private int telnet_port;//telnet端口号
	private  int sql_port;//数据库端口号
	private String sql_username;//数据库用户名
	private String sql_password;//数据库密码
	private String sql_db;//数据库名称
	private String sql_cmd_dir;//sql cmd 命令 目录
	private ServerType serverType;//服务器类型
	private OnlineServerAgent agent_online_server;
	private NewServerAgent agent_new_server;
	private int state = 1;
	private int sort = 0;
	private int server_instance;
	private int server_id;
	private int gm_port;
	private ClientNet net;
	public Server() {
		net = new ClientNet(this);
		init();
	}
	public void init() {
		agent_online_server = new OnlineServerAgent(this);
		agent_new_server = new NewServerAgent(this);
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSsh_host() {
		return ssh_host;
	}

	public void setSsh_host(String ssh_host) {
		this.ssh_host = ssh_host;
	}

	public int getSsh_port() {
		return ssh_port;
	}

	public void setSsh_port(int ssh_port) {
		this.ssh_port = ssh_port;
	}

	public String getSsh_username() {
		return ssh_username;
	}

	public void setSsh_username(String ssh_username) {
		this.ssh_username = ssh_username;
	}

	public String getSsh_password() {
		return ssh_password;
	}

	public void setSsh_password(String ssh_password) {
		this.ssh_password = ssh_password;
	}

	public String getSsh_remoteDir() {
		return ssh_remoteDir;
	}

	public void setSsh_remoteDir(String ssh_remoteDir) {
		this.ssh_remoteDir = ssh_remoteDir;
	}

	public String getSsh_localDir() {
		return ssh_localDir;
	}

	public void setSsh_localDir(String ssh_localDir) {
		this.ssh_localDir = ssh_localDir;
	}

	public String getTelnet_host() {
		return telnet_host;
	}

	public void setTelnet_host(String telnet_host) {
		this.telnet_host = telnet_host;
	}

	public int getTelnet_port() {
		return telnet_port;
	}

	public void setTelnet_port(int telnet_port) {
		this.telnet_port = telnet_port;
	}

	
	public ServerType getServerType() {
		return serverType;
	}

	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}

	public ClientNet getNet() {
		return net;
	}
	public void setNet(ClientNet net) {
		this.net = net;
	}
	public NewServerAgent getAgent_new_server() {
		return agent_new_server;
	}
	public void setAgent_new_server(NewServerAgent agent_new_server) {
		this.agent_new_server = agent_new_server;
	}
	public OnlineServerAgent getAgent_online_server() {
		return agent_online_server;
	}
	public void setAgent_online_server(OnlineServerAgent agent_online_server) {
		this.agent_online_server = agent_online_server;
	}
	@Override
	public void loadFromData(Map<String, Object> datas) {
		id = (Integer) datas.get(SERVER_ID);
		name = (String) datas.get(SERVER_NAME);
		groupId = (Integer) datas.get(SERVER_GROUP_ID);//组ID
		groupName = (String) datas.get(SERVER_GROUP_NAME);//组名字
		ssh_host = (String)datas.get(SERVER_SSH_HOST);
		ssh_port = (Integer)datas.get(SERVER_SSH_PORT);
		ssh_username = (String)datas.get(SERVER_SSH_USERNAME);
		ssh_password = (String)datas.get(SERVER_SSH_PASSWROD);
		ssh_remoteDir = (String)datas.get(SERVER_SSH_REMOTE_DIR);
		ssh_localDir = (String)datas.get(SERVER_SSH_LOCAL_DIR);
		telnet_host = (String)datas.get(SERVER_TELNET_HOST);
		telnet_port = (Integer)datas.get(SERVER_TELNET_PORT);
		serverType  = ServerType.valueOf((Integer)datas.get(SERVER_SERVER_TYPE));
		sql_port = (Integer)datas.get(SERVER_SQL_PORT);
		sql_username = (String)datas.get(SERVER_SQL_USERNAME);
		sql_password = (String)datas.get(SERVER_SQL_PASSWORD);
		sql_db = (String)datas.get(SERVER_SQL_DB);
		sql_cmd_dir = (String)datas.get(SERVER_SQL_CMD_DIR);
		server_instance = datas.get(SERVER_INSTANCE) == null ? -1 : (Integer)datas.get(SERVER_INSTANCE);
		server_id = datas.get(SERVER_SID) == null ? -1 : (Integer)datas.get(SERVER_SID);
		gm_port = datas.get(SERVER_GM_PORT) == null ? 10001 : (Integer)datas.get(SERVER_GM_PORT);
		
		//解密
		ssh_password = SshUtil.getStringEOR(ssh_password, Const.EOR_KEY);
		sql_password = SshUtil.getStringEOR(sql_password, Const.EOR_KEY);
	}
	
	
	public int getSql_port() {
		return sql_port;
	}
	public void setSql_port(int sql_port) {
		this.sql_port = sql_port;
	}
	public String getSql_username() {
		return sql_username;
	}
	public void setSql_username(String sql_username) {
		this.sql_username = sql_username;
	}
	public String getSql_password() {
		return sql_password;
	}
	public void setSql_password(String sql_password) {
		this.sql_password = sql_password;
	}
	
	public String getSql_db() {
		return sql_db;
	}
	public void setSql_db(String sql_db) {
		this.sql_db = sql_db;
	}
	
	public String getSql_cmd_dir() {
		return sql_cmd_dir;
	}
	public void setSql_cmd_dir(String sql_cmd_dir) {
		this.sql_cmd_dir = sql_cmd_dir;
	}
	@Override
	public void saveToData(Map<String, Object> datas) {
		datas.put(SERVER_ID, id);
		datas.put(SERVER_GROUP_ID, groupId);
		datas.put(SERVER_GROUP_NAME, groupName);
		datas.put(SERVER_NAME, name);
		datas.put(SERVER_SSH_HOST, ssh_host);
		datas.put(SERVER_SSH_PORT, ssh_port);
		datas.put(SERVER_SSH_USERNAME, ssh_username);
		datas.put(SERVER_SSH_PASSWROD, SshUtil.getStringEOR(ssh_password, Const.EOR_KEY));
		datas.put(SERVER_SSH_REMOTE_DIR, ssh_remoteDir);
		datas.put(SERVER_SSH_LOCAL_DIR, ssh_localDir);
		datas.put(SERVER_TELNET_HOST, telnet_host);
		datas.put(SERVER_TELNET_PORT, telnet_port);
		datas.put(SERVER_SERVER_TYPE, serverType.ordinal());
		datas.put(SERVER_SQL_PORT, sql_port);
		datas.put(SERVER_SQL_USERNAME, sql_username);
		datas.put(SERVER_SQL_PASSWORD, SshUtil.getStringEOR(sql_password, Const.EOR_KEY));
		datas.put(SERVER_SQL_DB, sql_db);
		datas.put(SERVER_SQL_CMD_DIR, sql_cmd_dir);
		datas.put(SERVER_STATE, state);
		datas.put(SERVER_SORT,sort);
		datas.put(SERVER_INSTANCE,server_instance);
		datas.put(SERVER_SID,server_id);
		datas.put(SERVER_GM_PORT,gm_port);
		
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getServer_instance() {
		return server_instance;
	}
	public void setServer_instance(int server_instance) {
		this.server_instance = server_instance;
	}
	public String toString() {
		return String.format(" %s:%s ",id == 0 ? "待" : id+"",name);
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public int getServer_id() {
		return server_id;
	}
	public String getSimpleDir() {
		String[] temps = getSsh_remoteDir().split("/");
		return temps[temps.length-1];
	}
	public int getGm_port() {
		return gm_port;
	}
	public void setGm_port(int gm_port) {
		this.gm_port = gm_port;
	}
	public void setServer_id(int server_id) {
		this.server_id = server_id;
	}

	public Server clone() {
		Server clone = new Server();
		clone.id = id;//服务器id
		clone.name= name;// 服务器名称
		clone.groupId = groupId;//组ID
		clone.groupName = groupName;//组名字
		clone.ssh_host = ssh_host;//地址
		clone.ssh_port = ssh_port;//端口号
		clone.ssh_username = ssh_username;//用户名
		clone.ssh_password = ssh_password;//密码
		clone.ssh_remoteDir = ssh_remoteDir;//远程上传下载目录
		clone.ssh_localDir = ssh_localDir;//本地下载目录
		clone.telnet_host = telnet_host;//telnet地址
		clone.telnet_port = telnet_port;//telnet端口号
		clone.sql_port = sql_port;//数据库端口号
		clone.sql_username = sql_username;//数据库用户名
		clone.sql_password = sql_password;//数据库密码
		clone.sql_db = sql_db;//数据库名称
		clone.sql_cmd_dir = sql_cmd_dir;//sql cmd 命令 目录
		clone.serverType = serverType;//服务器类型
		clone.state = state; 
		clone.sort = sort;
		clone.server_instance = server_instance;
		clone.server_id = server_id;
		clone.gm_port = gm_port;
		return clone;
	}
	
	public static void main(String[] args) {
		 Scanner s = new Scanner(System.in);
		 System.out.print("输入密码：");
		  String str =  s.next();
		System.out.println(SshUtil.getStringEOR(str, Const.EOR_KEY));
	}
}

/**
 * 
 */
package com.zyj.v1.ssh.db;

import java.util.Map;


/**
 * 需要保存到数据库的接口，
 * 只用作update
 * @author Dream
 *
 */
public interface DaoData {
	//server
	String SERVER_ID = "id";
	String SERVER_NAME = "name";
	String SERVER_GROUP_ID = "group_id";
	String SERVER_GROUP_NAME = "group_name";
	String SERVER_STATE = "state";
	String SERVER_SORT = "sort";
	String SERVER_INSTANCE = "server_instance";
	String SERVER_SID = "server_id";
	String SERVER_GM_PORT = "gm_port";
	String SERVER_SSH_HOST = "ssh_host";
	String SERVER_SSH_PORT = "ssh_port";
	String SERVER_SSH_USERNAME = "ssh_username";
	String SERVER_SSH_PASSWROD = "ssh_password";
	String SERVER_SSH_REMOTE_DIR = "ssh_remoteDir";
	String SERVER_SSH_LOCAL_DIR = "ssh_localDir";
	String SERVER_TELNET_HOST = "telnet_host";
	String SERVER_TELNET_PORT = "telnet_port";
	String SERVER_SERVER_TYPE = "servertype";
	String SERVER_SQL_PORT = "sql_port";
	String SERVER_SQL_USERNAME = "sql_username";
	String SERVER_SQL_PASSWORD = "sql_password";
	String SERVER_SQL_DB = "sql_db";
	String SERVER_SQL_CMD_DIR = "sql_cmd_dir";
	
	
	//gm_user
	String TABLE_GM_USER = "gm_user";
	String GM_USER_ID = "id";
	String GM_USER_NAME = "name";
	String GM_USER_PASSWROD = "password";
	String GM_USER_RIGHT = "right";
		
		
	void loadFromData(Map<String,Object> map);
	void saveToData(Map<String,Object> map);
	
}

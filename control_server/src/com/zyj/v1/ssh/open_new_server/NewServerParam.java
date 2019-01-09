package com.zyj.v1.ssh.open_new_server;

public enum NewServerParam {
	APP_PATH("服务器路径",0),
	APP_INSTANCE("服务器实例号",0),
	APP_NAME("服务器名字",0),
	APP_ID("服务器编号",0),
	GM_CONSOLE_PORT("GM端口号",0),
	APP_IP("IP地址",0),
	SSH_PORT("SSH端口号",0),
	SSH_USERNAME("SSH用户名",0),
	SSH_PASSWORD("SSH密码",1),
	TELNET_PORT("TELNET端口号",0),
	DB_NAME("DB名字",0),
	SQL_USERNAME("SQL账号",0),
	SQL_PASSWORD("SQL密码",1),
	SQL_PORT("DB端口号",0),
	SQL_CMD_DIR("MYSQL命令目录",0),
	;
	String chName;
	byte type;//0：普通，不需要处理的，1：密码类型，需要加密 
	NewServerParam(String name,int type) {
		this.chName = name;
		this.type = (byte)type;
	}
	public String getChName() {
		return chName;
	}
	public static NewServerParam getVal(String val) {
		try {
			return NewServerParam.valueOf(val);
		} catch (Exception e) {
			return null;
		}
	}
}

package com.zyj.v1.ssh.open_new_server;

public enum NewServerParam {
	APP_PATH("������·��",0),
	APP_INSTANCE("������ʵ����",0),
	APP_NAME("����������",0),
	APP_ID("���������",0),
	GM_CONSOLE_PORT("GM�˿ں�",0),
	APP_IP("IP��ַ",0),
	SSH_PORT("SSH�˿ں�",0),
	SSH_USERNAME("SSH�û���",0),
	SSH_PASSWORD("SSH����",1),
	TELNET_PORT("TELNET�˿ں�",0),
	DB_NAME("DB����",0),
	SQL_USERNAME("SQL�˺�",0),
	SQL_PASSWORD("SQL����",1),
	SQL_PORT("DB�˿ں�",0),
	SQL_CMD_DIR("MYSQL����Ŀ¼",0),
	;
	String chName;
	byte type;//0����ͨ������Ҫ����ģ�1���������ͣ���Ҫ���� 
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

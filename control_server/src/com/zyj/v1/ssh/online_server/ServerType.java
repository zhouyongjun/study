package com.zyj.v1.ssh.online_server;
	
/**
 * ����������
 * @author zhouyongjun
 *
 */
public enum ServerType {
	NO_NET("������������"),
	MAIN_NET("��������"),
	SUB_NET("�ַ�����"),
	;
	String name;
	ServerType(String name) {
		this.name = name;
	}
	public static ServerType valueOf(int index) {
		return ServerType.values()[index];
	}
}

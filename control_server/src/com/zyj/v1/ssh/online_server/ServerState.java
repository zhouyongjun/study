package com.zyj.v1.ssh.online_server;
	/**
	 * �߼�ִ��״̬
	 * @author zhouyongjun
	 *
	 */
public enum ServerState {
	NOTHING("��״̬"),
	START_UP("����"),
	SHUT_DOWN("ͣ��"),
	FREEZE("��������"),
	;
	String name;
	ServerState(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

package com.zyj.v1.ssh.online_server;
	/**
	 * 逻辑执行状态
	 * @author zhouyongjun
	 *
	 */
public enum ServerState {
	NOTHING("无状态"),
	START_UP("开服"),
	SHUT_DOWN("停服"),
	FREEZE("保存数据"),
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

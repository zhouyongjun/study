package com.zyj.v1.ssh.online_server;
	
/**
 * 服务器类型
 * @author zhouyongjun
 *
 */
public enum ServerType {
	NO_NET("非线网服务器"),
	MAIN_NET("主服务器"),
	SUB_NET("分服务器"),
	;
	String name;
	ServerType(String name) {
		this.name = name;
	}
	public static ServerType valueOf(int index) {
		return ServerType.values()[index];
	}
}

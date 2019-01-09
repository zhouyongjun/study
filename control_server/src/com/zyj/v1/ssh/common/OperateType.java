package com.zyj.v1.ssh.common;

public enum OperateType {
	SERVER_MAINTAIN("服务器维护操作"),
	UPLOAD("上传操作"),
	DOWNLOAD("下载操作");
	;
	String name;
	OperateType(String name) {
		this.name = name;
	}
}

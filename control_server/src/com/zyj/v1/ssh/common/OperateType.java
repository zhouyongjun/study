package com.zyj.v1.ssh.common;

public enum OperateType {
	SERVER_MAINTAIN("������ά������"),
	UPLOAD("�ϴ�����"),
	DOWNLOAD("���ز���");
	;
	String name;
	OperateType(String name) {
		this.name = name;
	}
}

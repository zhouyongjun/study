package com.zyj.v1.ssh.common;

import java.util.Map;

import com.zyj.v1.ssh.db.DaoData;

public class ClientUser implements DaoData{
	private int id;
	private String name;
	private String pwd;
	private int right;
	
	@Override
	public void loadFromData(Map<String, Object> datas) {
		id = (Integer) datas.get(GM_USER_ID);
		name = (String) datas.get(GM_USER_NAME);
		pwd = (String) datas.get(GM_USER_PASSWROD);
		right = (Integer) datas.get(GM_USER_ID);
	}
	@Override
	public void saveToData(Map<String, Object> datas) {
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getRight() {
		return right;
	}
	public void setRight(int right) {
		this.right = right;
	}
	
}

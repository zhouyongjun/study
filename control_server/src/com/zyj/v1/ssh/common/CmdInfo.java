package com.zyj.v1.ssh.common;

public class CmdInfo {
	private String cmd;
	private String showName;
	private String return_true_msg;
	private String return_error_msg;
	private int sort;
	
	public CmdInfo(String cmd,String showName) {
		this(cmd, showName, null,null);
	}
	public CmdInfo(String cmd,String showName,String return_true_msg,String return_error_msg) {
		this.cmd = cmd;
		this.showName = showName;
		this.return_true_msg = return_true_msg;
		this.return_error_msg = return_error_msg;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	public String getShowName() {
		return showName;
	}
	public void setShowName(String showName) {
		this.showName = showName;
	}
	
	public String getReturn_true_msg() {
		return return_true_msg;
	}
	public void setReturn_true_msg(String return_true_msg) {
		this.return_true_msg = return_true_msg;
	}
	public String getReturn_error_msg() {
		return return_error_msg;
	}
	public void setReturn_error_msg(String return_error_msg) {
		this.return_error_msg = return_error_msg;
	}
	
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@Override
	public String toString() {
		return "√¸¡Ó°æ"+showName+","+cmd+"°ø";
	}
	
	
	
}

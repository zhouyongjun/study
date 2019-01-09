package com.zyj.v1.ssh.log_control;
	/**
	 * 日志状态类型
	 * @author zhouyongjun
	 *
	 */
public class LogControl {
	private String control_msg;//监控信息
	private String showName;//展现
	private boolean isThrougth;//是否通过
	
	public LogControl(String msg,String showName) {
		this.control_msg = msg.toLowerCase();
		this.showName = showName;
	}

	public String getControl_msg() {
		return control_msg;
	}

	public void setControl_msg(String control_msg) {
		this.control_msg = control_msg;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public boolean isThrougth() {
		return isThrougth;
	}

	public void setThrougth(boolean isThrougth) {
		this.isThrougth = isThrougth;
	}
	
}

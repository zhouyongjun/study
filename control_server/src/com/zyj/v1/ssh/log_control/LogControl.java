package com.zyj.v1.ssh.log_control;
	/**
	 * ��־״̬����
	 * @author zhouyongjun
	 *
	 */
public class LogControl {
	private String control_msg;//�����Ϣ
	private String showName;//չ��
	private boolean isThrougth;//�Ƿ�ͨ��
	
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

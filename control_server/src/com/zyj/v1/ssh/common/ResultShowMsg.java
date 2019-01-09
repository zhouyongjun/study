package com.zyj.v1.ssh.common;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
	/**
	 * 结果信息
	 * @author zhouyongjun
	 *
	 */
public class ResultShowMsg {
	SimpleAttributeSet attrSet;
	String msg;
	public ResultShowMsg(String msg, Color color, int font) {
		this.msg = msg;
		attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, color);
		StyleConstants.setFontSize(attrSet, font); // 字体大小
	}
	public SimpleAttributeSet getAttrSet() {
		return attrSet;
	}
	public void setAttrSet(SimpleAttributeSet attrSet) {
		this.attrSet = attrSet;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}

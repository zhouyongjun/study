package com.zhou.core.email;

import java.util.ArrayList;
import java.util.List;

import com.zhou.util.sfs.data.ISFSObject;
import com.zhou.util.sfs.data.SFSObject;
	/**
	 * 邮件信息内容实体类
	 * @author zhouyongjun
	 *
	 */
public class EmailMsg {
	private String hint;
	private List<String> texts;
	
	public EmailMsg(String hint) {
		this.hint = hint;
		texts = new ArrayList<String>();
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
	/**
	 * 信息组合
	 * @return
	 */
	public String toText() {
		if (texts.isEmpty()) return "";
		StringBuffer sb = new StringBuffer();
		for (String text : texts) {
			sb.append(text).append("\n")
			.append("---------------------------------------------------------\n");
		}
		return sb.toString();
	}

	public List<String> getTexts() {
		return texts;
	}

	public void setTexts(List<String> texts) {
		this.texts = texts;
	}

	public void addText(String text) {
		texts.add(text);
	}
	
	public String toString() {
		return new StringBuilder().append(this.getClass().getSimpleName()).append("[hint=")
				.append(hint).append(",size = ").append(texts.size()).append("]").toString();
	}
	public String toDetailString() {
		ISFSObject object = SFSObject.newInstance();
		object.putUtfString("className", this.getClass().getSimpleName());
		object.putUtfString("hint", hint);
		object.putInt("size", texts.size());
		object.putUtfString("text", toText());
		return object.toJson();
	}
	
	public boolean isEmpty() {
		return texts.isEmpty();
	}

	public int size() {
		return texts.size();
	}
}

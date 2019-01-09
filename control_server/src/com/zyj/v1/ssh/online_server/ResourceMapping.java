package com.zyj.v1.ssh.online_server;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.zyj.v1.ssh.common.CmdInfo;
import com.zyj.v1.ssh.util.SshUtil;

public class ResourceMapping {
	private String name;//Ãû³Æ
	private String path;//Â·¾¶
	List<CmdInfo> cmd_infos;
	public ResourceMapping() {
		
	}
	public void load(Element e) {
		name = SshUtil.getAttriValue(e, "name");
		path = SshUtil.getAttriValue(e, "path");
		loadTelnet(SshUtil.getElements(e, "telnet"));
	}
	public void loadTelnet(List<Element> elements) {
		if (elements == null || elements.size() == 0) {
			return;
		}
		cmd_infos = new ArrayList<CmdInfo>();
		for (Element e : elements) {
			String telnet_cmd = SshUtil.getAttriValue(e, "cmd");//telnet ÃüÁî
			if (telnet_cmd == null || telnet_cmd.length() == 0) {
				continue;
			}
			String return_sign = SshUtil.getAttriValue(e, "return_sign");
			String temp_port =  SshUtil.getAttriValue(e, "cmd_sort");
			int sort = 0;
			if (temp_port != null && temp_port.length() > 0) {
				sort = Integer.parseInt(temp_port);
			}
			
			CmdInfo cmd = new CmdInfo(telnet_cmd, telnet_cmd);
			cmd.setReturn_true_msg(return_sign);
			cmd.setSort(sort);
			cmd_infos.add(cmd);
		}
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<CmdInfo> getCmd_infos() {
		return cmd_infos;
	}
	public void setCmd_infos(List<CmdInfo> cmd_infos) {
		this.cmd_infos = cmd_infos;
	}
	
}

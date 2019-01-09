package com.zyj.v1.ssh.open_new_server;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.zyj.v1.ssh.common.CONF_PATH;
import com.zyj.v1.ssh.util.SshUtil;

public class TableOldServerScpDirs extends AbstractTableConfig{
	public static final String FILE_NAME = CONF_PATH.PATH_NEW_SERVER+"table_old_server_scp_dirs.xml";
	public void load() {
		Document doc = SshUtil.load(FILE_NAME);
		List<Element> els = SshUtil.getElements(doc.getRootElement(), "config");
		String[][] temp_vals = new String[els.size()][1];
		for (int i=0;i<els.size();i++) {
			Element e = els.get(i);
			temp_vals[i] = new String[]{SshUtil.getAttriValue(e, "value")};
		}
		this.configs = temp_vals;
	}
}

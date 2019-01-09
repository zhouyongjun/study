package com.zyj.v1.ssh.open_new_server;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.zyj.v1.ssh.common.CONF_PATH;
import com.zyj.v1.ssh.util.SshUtil;

public class TableNewServerAutoIncrement extends AbstractTableConfig {
	public static final String FILE_NAME = CONF_PATH.PATH_NEW_SERVER+"table_new_server_auto_increment.xml";
	public void load() {
		Document doc = SshUtil.load(FILE_NAME);
		List<Element> els = SshUtil.getElements(doc.getRootElement(), "config");
		String[][] temp_autos = new String[els.size()][3];
		for (int i=0;i<els.size();i++) {
			Element e = els.get(i);
			String table = SshUtil.getAttriValue(e, "table");
			int add_val = Integer.parseInt(SshUtil.getAttriValue(e, "add_val"));
			int init_val = Integer.parseInt(SshUtil.getAttriValue(e, "init_val"));
			temp_autos[i] = new String[]{table,add_val+"",init_val+""};
		}
		this.configs = temp_autos;
	}
	
}

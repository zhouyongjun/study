package com.zyj.v1.ssh.open_new_server;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.zyj.v1.ssh.common.CONF_PATH;
import com.zyj.v1.ssh.util.SshUtil;
	/**
	 * 新区参数配置
	 * @author zhouyongjun
	 *
	 */
public class TableNewServerParams extends AbstractTableConfig{
	public static final String FILE_NAME = CONF_PATH.PATH_NEW_SERVER+"table_new_server_params.xml";
	private NewServerParam[] new_server_params;
	public void load() {
		Document doc = SshUtil.load(FILE_NAME);
		List<Element> els = SshUtil.getElements(doc.getRootElement(), "config");
		String[][] temp_vals = new String[els.size()][2];
		NewServerParam[] temp_params = new NewServerParam[els.size()];
		for (int i=0;i<els.size();i++) {
			Element e = els.get(i);
			temp_vals[i] = new String[]{SshUtil.getAttriValue(e, "name"),SshUtil.getAttriValue(e, "value"),SshUtil.getAttriValue(e, "isSave")};
			temp_params[i] = NewServerParam.valueOf(SshUtil.getAttriValue(e, "key"));
		}
		this.configs = temp_vals;
		this.new_server_params = temp_params;
	}
	public NewServerParam[] getNew_server_params() {
		return new_server_params;
	}
	public void setNew_server_params(NewServerParam[] new_server_params) {
		this.new_server_params = new_server_params;
	}
	
	public String getValue(int i,int j) {
		return configs[i][j];
	}

}

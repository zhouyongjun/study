package com.zyj.v1.ssh.open_new_server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.zyj.v1.ssh.common.CONF_PATH;
import com.zyj.v1.ssh.util.SshUtil;

/**
 * 新区文件修改
 * @author zhouyongjun
 *
 */
public class TableNewServerUpdateFiles extends AbstractTableConfig{
	public static final String FILE_NAME = CONF_PATH.PATH_NEW_SERVER+"table_new_server_files_config.xml";
	private Map<String,String[][]> patternMaps = new HashMap<String,String[][]>();
	public void load() {
		Document doc = SshUtil.load(FILE_NAME);
		List<Element> els = SshUtil.getElements(doc.getRootElement(), "config");
		String[][] temp_vals = new String[els.size()][2];
		for (int i=0;i<els.size();i++) {
			Element e = els.get(i);
			String file = SshUtil.getAttriValue(e, "file");
			List<Element> updateEls = SshUtil.getElements(e, "update");
			String[][] parrtenValues = new String[updateEls.size()][];
			StringBuffer showSb = new StringBuffer();
			for (int j=0;j<updateEls.size();j++) {
				Element updateE = updateEls.get(j);
				parrtenValues[j] = new String[]{SshUtil.getAttriValue(updateE, "pattern"),SshUtil.getAttriValue(updateE, "replace")};
				showSb.append(SshUtil.getAttriValue(updateE, "name")).append("\\|");
			}
			temp_vals[i] = new String[]{file,showSb.toString()};
			patternMaps.put(file, parrtenValues);
		}
		this.configs = temp_vals;
	}
	public Map<String, String[][]> getPatternMaps() {
		return patternMaps;
	}
	public void setPatternMaps(Map<String, String[][]> patternMaps) {
		this.patternMaps = patternMaps;
	}
	
}

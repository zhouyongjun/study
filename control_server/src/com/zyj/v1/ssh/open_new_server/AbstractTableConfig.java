package com.zyj.v1.ssh.open_new_server;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTableConfig {
	protected String[][] configs = new String[1][3];
	public abstract void load();
	public String[][] getConfigs() {
		return configs;
	}
	public void setConfigs(String[][] configs) {
		this.configs = configs;
	}
	
	public List<String> getArrayFirstElementList(String... params) {
		List<String> list = new ArrayList<String>();
		for (String[] vals : getConfigs()) {
			if (vals == null || vals.length == 0) {
				continue;
			}
			String config = vals[0];
			if (config == null || config.length() == 0) {
				continue;
			}
			if (params != null && params.length > 0) {
				config = params[0] + config;
			}
			list.add(config);
		}
		return list;
	}
}	

package com.zhou.core.whitelist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.ToolsApp;
import com.zhou.ToolsApp.ConsoleSystem;
import com.zhou.ToolsApp.SystemType;
import com.zhou.config.ToolsConfig;
import com.zhou.core.gm.cs.log.GmLog;

/**
 * 白名单
 * @author zhouyongjun
 *
 */
public final class WhitelistService implements ConsoleSystem {
	private static final Logger logger = LogManager.getLogger(WhitelistService.class);
	private static WhitelistService instance = new WhitelistService();
	List<String> whiteList = new ArrayList<String>();
	protected boolean isStartUp;
	private WhitelistService() {
		
	}
	
	public static WhitelistService getInstance() {
		return instance;
	}
	/**
	 * 是否包含
	 * @param line
	 * @return
	 */
	public boolean contians(String line) {
		if (!ToolsConfig.WHITE_LIST_SWTICH) return true;
		return whiteList.contains(line);
	}
	
	public void startup(Object... params) throws Throwable {
		if (!ToolsApp.getInstance().isConfigLoaded())
		{
			ToolsApp.getInstance().loadConfigPropeties();
		}
		String pathfile = WhitelistService.class.getClassLoader().getResource(ToolsConfig.WHITE_LIST_FILE_NAME).getPath();
		whiteList = readLinesFromFile(pathfile);
		isStartUp = true;
	}
	
	public void shutdown(Object... params) throws Throwable {
		whiteList.clear();
		isStartUp = false;
	}
	
	public List<String> readLinesFromFile(String pathfile) {
		logger.info("<<<<<<<<<<<<<load properties:"+pathfile);
		List<String> temp_list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(pathfile)),"utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.equals(""))continue;
				temp_list.add(line);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (br != null)try {br.close();} catch (IOException e) {}
			
		}
		return temp_list;
	}

	public List<String> getWhiteList() {
		return whiteList;
	}

	@Override
	public SystemType getConsoleSubSystemType() {
		return SystemType.WHITE_LIST;
	}
	
	@Override
	public boolean isStartUp() {
		return isStartUp;
	}	
}

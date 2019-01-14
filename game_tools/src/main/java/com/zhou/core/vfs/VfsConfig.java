package com.zhou.core.vfs;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.zhou.config.ToolsConfig;
import com.zhou.util.CalendarUtil;
import com.zhou.util.StringUtils;
import com.zhou.util.XmlUtils;
/**
 * 自动检测配置
 * @author zhouyongjun
 *
 */
public final class VfsConfig {
	private static final Logger logger = LogManager.getLogger(VfsConfig.class);
	public static boolean VFS_START_SWITCH = true;//检测开关
	public static long DELAY = 1 * CalendarUtil.SECOND;//检测间隔时间
	/*
	 * 检测文件范围
	 */
	public static String[] VFS_PATH_FILES = {ToolsConfig.VFS_CONFIG_FILE_NAME};
	/*
	 * 文件检测类
	 */
	
	public static String[] FILE_EVENT_CLASS_NAMES = new String[0];
	
	public static void load() throws Throwable
	{
		loadXml();
	}
	public static void loadXml()throws Throwable{
		String path = VfsConfig.class.getClassLoader().getResource(ToolsConfig.VFS_CONFIG_FILE_NAME).getPath();
		logger.info("<<<<<<<<<<<<<load file:"+path);
		Document doc  = XmlUtils.load(path);
		Element rootE = doc.getDocumentElement();
		VFS_START_SWITCH = StringUtils.isBoolean(rootE.getAttribute("vfs_switch"));
		DELAY = StringUtils.getInt(rootE.getAttribute("interval_second")) * CalendarUtil.SECOND;
		if (DELAY < CalendarUtil.SECOND) DELAY = CalendarUtil.SECOND;
		_loadPathFiles(XmlUtils.getChildrenByName(rootE, "file"));
		_loadFileEvents(XmlUtils.getChildrenByName(rootE, "event"));
	}
	
	private static void _loadFileEvents(Element[] els) {
		List<String> temp = new ArrayList<>();
		for (Element e : els) 
		{
			temp.add(XmlUtils.getAttribute(e, "class_name"));
		}
		FILE_EVENT_CLASS_NAMES = new String[temp.size()];
		temp.toArray(FILE_EVENT_CLASS_NAMES);
	}
	
	private static void _loadPathFiles(Element[] els) {
		List<String> temp = new ArrayList<>();
		for (Element e : els) 
		{
			temp.add(XmlUtils.getAttribute(e, "name"));
		}
		VFS_PATH_FILES = new String[temp.size()];
		temp.toArray(VFS_PATH_FILES);
	}
	
}

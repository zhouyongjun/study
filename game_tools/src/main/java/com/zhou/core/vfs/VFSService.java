package com.zhou.core.vfs;

import java.io.File;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.ToolsApp;
import com.zhou.ToolsApp.ConsoleSystem;
import com.zhou.ToolsApp.SystemType;
import com.zhou.core.vfs.listen.FileListenerImpl;

public final class VFSService implements ConsoleSystem{
	private static final Logger logger = LogManager.getLogger(VFSService.class);
	boolean isStartUp;	
	private DefaultFileMonitor fileMoitor;
	private FileListenerImpl listener;
	private static VFSService instance = new VFSService();
	private VFSService() {
		// TODO Auto-generated constructor stub
	}
	public static VFSService getInstance()
	{
		return instance;
	}
	@Override
	public SystemType getConsoleSubSystemType() {
		return SystemType.VFS;
	}

	@Override
	public void startup(Object... params) throws Throwable {
		try {
			if (!ToolsApp.getInstance().isConfigLoaded())
			{
				ToolsApp.getInstance().loadConfigPropeties();
			}
			VfsConfig.load();
			if (!VfsConfig.VFS_START_SWITCH) {
				logger.info("  vfs auto load switch is close,so fail startup.....");
				return;
			}
			logger.info("  vfs auto load is starting.....");
			shutdown(params);
			FileSystemManager mgr = VFS.getManager(); 
			listener = new FileListenerImpl();
			listener.registEvents(VfsConfig.FILE_EVENT_CLASS_NAMES);
			fileMoitor = new DefaultFileMonitor(listener);
			//设置检测间隔时间
			fileMoitor.setDelay(VfsConfig.DELAY);
			fileMoitor.setRecursive(true);
			for (String name : VfsConfig.VFS_PATH_FILES) {
				File file = new File(name);
				String path = file.getAbsolutePath();
				FileObject fileObject = mgr.resolveFile(path);
				fileMoitor.addFile(fileObject);
			}
			fileMoitor.start();
		} catch (Exception e) {
			logger.error(e);
		}
		isStartUp = true;	
	}

	@Override
	public void shutdown(Object... params) throws Throwable {
		 if(fileMoitor != null)  fileMoitor.stop();
		isStartUp =false;
	}

	@Override
	public boolean isStartUp() {
		return isStartUp;
	}

	
	public FileListenerImpl getFileListener() {
		return listener;
	}
}

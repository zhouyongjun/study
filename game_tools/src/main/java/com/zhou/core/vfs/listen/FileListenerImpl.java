package com.zhou.core.vfs.listen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zhou.core.vfs.listen.event.VfsFileEvent;

public class FileListenerImpl implements FileListener{
	List<VfsFileEvent> events = new ArrayList<>();
	private static final Logger logger = LogManager.getLogger(FileListenerImpl.class);
	@Override
	public void fileChanged(FileChangeEvent arg0) throws Exception {
		//文件时间有改变才会更新，如果向文件写数据，但是时间未改变，则也是检测不到
		FileObject fileObject = arg0.getFile();
		try {
			logger.info("VFS AUTO LOAD CONSOLE CONFIG fileChanged:" + fileObject.getName());
			for (VfsFileEvent event : events)
			{
				if (event.fileChanged(fileObject))
				{
					break;
				}
			}
		} catch (Exception e) {
			logger.error("VFS AUTO FAIL LOAD FILE fileChanged:" + fileObject.getName(),e);
		}
	}

	@Override
	public void fileCreated(FileChangeEvent arg0) throws Exception {
		//文件时间有改变才会更新，如果向文件写数据，但是时间未改变，则也是检测不到
		FileObject fileObject = arg0.getFile();
		try {
			logger.info("VFS AUTO LOAD CONSOLE CONFIG fileCreated:" + fileObject.getName());
			for (VfsFileEvent event : events)
			{
				if (event.fileCreated(fileObject))
				{
					break;
				}
			}
		} catch (Exception e) {
			logger.error("VFS AUTO FAIL LOAD FILE fileCreated:" + fileObject.getName(),e);
		}
	}

	@Override
	public void fileDeleted(FileChangeEvent arg0) throws Exception {
		FileObject fileObject = arg0.getFile();
		try {
			logger.info("VFS AUTO LOAD CONSOLE CONFIG  fileDeleted:" + fileObject.getName());
			for (VfsFileEvent event : events)
			{
				if (event.fileDeleted(fileObject))
				{
					break;
				}
			}
		} catch (Exception e) {
			logger.error("VFS AUTO FAIL LOAD FILE fileDeleted:" + fileObject.getName(),e);
		}
	}

	public List<VfsFileEvent> getEvents() {
		return events;
	}
	
	/**
	 * 增加事件
	 * @param event
	 */
	public void addEvent(VfsFileEvent event) {
		events.add(event);
	}
	/**
	 * 删除时间
	 * @param event
	 */
	public void removeEvent(VfsFileEvent event) {
		events.remove(event);
	}

	public void registEvents(String[] event_clz_names) throws Exception {
		for(String clz : event_clz_names){
			events.add((VfsFileEvent) Class.forName(clz).newInstance());
		}
	}
	

	
}

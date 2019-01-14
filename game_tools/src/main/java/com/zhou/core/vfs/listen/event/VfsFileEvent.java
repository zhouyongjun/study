package com.zhou.core.vfs.listen.event;

import org.apache.commons.vfs2.FileObject;

	/**
	 * 文件变化触发事件
	 * @author zhouyongjun
	 *
	 */
public interface VfsFileEvent {
	/**
	 * 文件改变
	 * @param file
	 * @throws Exception
	 */
	public boolean fileChanged(FileObject file) throws Exception ;
	/**
	 * 文件创建
	 * @param file
	 * @throws Exception
	 */
	public boolean fileCreated(FileObject file) throws Exception ;
	/**
	 * 文件删除
	 * @param file
	 * @throws Exception
	 */
	public boolean fileDeleted(FileObject file) throws Exception ;
}

package com.zhou.core.vfs.listen.event;

import org.apache.commons.vfs2.FileObject;

	/**
	 * �ļ��仯�����¼�
	 * @author zhouyongjun
	 *
	 */
public interface VfsFileEvent {
	/**
	 * �ļ��ı�
	 * @param file
	 * @throws Exception
	 */
	public boolean fileChanged(FileObject file) throws Exception ;
	/**
	 * �ļ�����
	 * @param file
	 * @throws Exception
	 */
	public boolean fileCreated(FileObject file) throws Exception ;
	/**
	 * �ļ�ɾ��
	 * @param file
	 * @throws Exception
	 */
	public boolean fileDeleted(FileObject file) throws Exception ;
}

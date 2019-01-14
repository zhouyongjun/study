package com.zhou.core.esb.server.entries;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.zhou.util.CParam;
/**
 * ��Ϸ������
 * @author zhouyongjun
 *
 */
public interface IGameServer {
	void init(CParam param);
	
	/**
	 * Ψһkey
	 * @return
	 */
	int getInstanceId();
	/**
	 * ����
	 * @return
	 */
	IoSession getIoSession();
	/**
	 * ��ȡ�����û���Ϣ
	 * @return
	 */
	Map<Long,IUser> getUsers();
	/**
	 * ��ȡ�û���Ϣ
	 * @param uid
	 * @return
	 */
	IUser getUser(long uid);
	/**
	 * �����û�
	 * @param user
	 * @return
	 */
	IUser addUser(IUser user);
	/**
	 * ɾ��
	 * @param uid
	 * @return
	 */
	IUser removeUser(long uid);
}



package com.zhou.core.esb.server.entries;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.zhou.util.CParam;
/**
 * 游戏服对象
 * @author zhouyongjun
 *
 */
public interface IGameServer {
	void init(CParam param);
	
	/**
	 * 唯一key
	 * @return
	 */
	int getInstanceId();
	/**
	 * 链接
	 * @return
	 */
	IoSession getIoSession();
	/**
	 * 获取所有用户信息
	 * @return
	 */
	Map<Long,IUser> getUsers();
	/**
	 * 获取用户信息
	 * @param uid
	 * @return
	 */
	IUser getUser(long uid);
	/**
	 * 增加用户
	 * @param user
	 * @return
	 */
	IUser addUser(IUser user);
	/**
	 * 删除
	 * @param uid
	 * @return
	 */
	IUser removeUser(long uid);
}



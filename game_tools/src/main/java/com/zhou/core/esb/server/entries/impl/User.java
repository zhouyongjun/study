package com.zhou.core.esb.server.entries.impl;

import org.apache.mina.core.session.IoSession;

import com.zhou.core.esb.server.entries.IUser;
import com.zhou.util.CParam;
	/**
	 * 用户信息
	 * @author zhouyongjun
	 *
	 */
public class User implements IUser{
	long uid;
	IoSession session;
	long logintime;
	@Override
	public long getUid() {
		return uid;
	}
	@Override
	public void init(CParam param) {
		logintime = System.currentTimeMillis();
	}
	@Override
	public IoSession getSession() {
		return session;
	}
	
}

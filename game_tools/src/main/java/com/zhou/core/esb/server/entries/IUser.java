package com.zhou.core.esb.server.entries;

import org.apache.mina.core.session.IoSession;

import com.zhou.util.CParam;

public interface IUser {
	void init(CParam param);
	long getUid();
	IoSession getSession();
}

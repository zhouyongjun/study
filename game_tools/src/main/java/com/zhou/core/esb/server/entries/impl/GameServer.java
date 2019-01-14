package com.zhou.core.esb.server.entries.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.zhou.core.esb.server.entries.IGameServer;
import com.zhou.core.esb.server.entries.IUser;
import com.zhou.util.CParam;

public class GameServer implements IGameServer{
	int instanceId;
	IoSession session;
	Map<Long,IUser> users;
	
	@Override
	public void init(CParam param) {
		instanceId = param.get("instanceId",Integer.class);
		session = param.get("session",IoSession.class);
		users = new HashMap<Long, IUser>();
	}
	@Override
	public int getInstanceId() {
		return instanceId;
	}
	@Override
	public IoSession getIoSession() {
		return session;
	}
	@Override
	public Map<Long, IUser> getUsers() {
		return users;
	}
	@Override
	public IUser getUser(long uid) {
		return users.get(uid);
	}
	@Override
	public IUser addUser(IUser user) {
		return users.put(user.getUid(), user);
	}
	@Override
	public IUser removeUser(long uid) {
		return users.remove(uid);
	}
}

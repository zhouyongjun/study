package com.zhou.study.spring.rmi.server;

public class GameServiceImpl implements GameService {
	
	@Override
	public String login(String user, String passwd) {
		return String.format("login success!{user=%s,passwd=%s}",user,passwd);
	}
	@Override
	public String logout(String user) {
		return String.format("logout success!{user=%s}",user);
	}

	@Override
	public String cmd(String cmd) {
		return String.format("{cmd=%s}",cmd);
	}

}

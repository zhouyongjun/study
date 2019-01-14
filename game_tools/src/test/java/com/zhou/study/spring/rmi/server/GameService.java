package com.zhou.study.spring.rmi.server;

public interface GameService {
	String login(String user,String passwd);
	String logout(String user);
	String cmd(String cmd);
}

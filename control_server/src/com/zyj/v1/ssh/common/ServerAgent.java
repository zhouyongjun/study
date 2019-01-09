package com.zyj.v1.ssh.common;

import com.zyj.v1.ssh.Server;

public abstract class ServerAgent {
	protected Server server;
	
	public ServerAgent(Server server) {
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}
	
	
}

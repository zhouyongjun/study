package com.zhou.core.esb.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.handler.chain.ChainedIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.zhou.core.esb.client.listen.ClientNetService;
import com.zhou.core.esb.common.Const;
import com.zhou.core.esb.common.EsbContext;
import com.zhou.core.esb.server.filter.decode.DefaultProtocolDecoder;
import com.zhou.core.esb.server.filter.encode.DefaultProtocolEncoder;

public class EsbClient {
	EsbContext context;
	NioSocketConnector conn;
	public void init()
	{
		
	}
	public boolean login()
	{
		conn = new NioSocketConnector();
		SocketAddress address = new InetSocketAddress("localhost",33333);
		conn.getFilterChain().addFirst("codec", new ProtocolCodecFilter(new DefaultProtocolEncoder(), new DefaultProtocolDecoder()));
		conn.getFilterChain().addLast("executor", new ExecutorFilter());
		conn.addListener(new ClientNetService());
		ChainedIoHandler handler = new ChainedIoHandler();
		handler.getChain().addFirst("command", new ClientIoCommand());
		conn.setHandler(handler);
		conn.connect(address);
		Const.log("EsbClient is start.",conn.isActive());
		return false;
	}
	public static void main(String[] args) {
		EsbClient client = new EsbClient();
		client.login();
	}
}

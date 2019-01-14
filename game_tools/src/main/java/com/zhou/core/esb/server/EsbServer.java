package com.zhou.core.esb.server;

import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.handler.chain.ChainedIoHandler;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.zhou.core.esb.common.Const;
import com.zhou.core.esb.server.filter.decode.DefaultProtocolDecoder;
import com.zhou.core.esb.server.filter.encode.DefaultProtocolEncoder;
import com.zhou.core.esb.server.listen.ServerNetService;
/**
 * 服务器的启动
 * @author zhouyongjun
 *
 */
public class EsbServer {
	
	public void start() throws Exception
	{
		NioSocketAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addFirst("codec", new ProtocolCodecFilter(new DefaultProtocolEncoder(),new DefaultProtocolDecoder()));
		acceptor.getFilterChain().addLast("excutor", new ExecutorFilter());
		acceptor.addListener(new ServerNetService());
		ChainedIoHandler handler = new ChainedIoHandler();
		handler.getChain().addFirst("command", new ServerIoCommand());
		acceptor.setHandler(handler);
		acceptor.bind(new InetSocketAddress("localhost",33333));
		Const.log("EsbServer is start.",acceptor.isActive());
	}
	public static void main(String[] args) {
		EsbServer server = new EsbServer();
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

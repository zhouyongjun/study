package com.zhou.study.thrift.server;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;

import com.zhou.study.thrift.calc.Calculator;
import com.zhou.study.thrift.calc.CalculatorImpl;

public class PoolServer
{
	public static final int PORT = 9988;
	
	public static void main(String[] args)throws Exception
	{
		//传送层
		TServerSocket serverSocket = new TServerSocket(PORT);
		//处理层
		TMultiplexedProcessor mp = new TMultiplexedProcessor();
		//协议层
		TProtocolFactory protocolFactory = new TCompactProtocol.Factory();  
		mp.registerProcessor("Calculator", new Calculator.Processor<>(new CalculatorImpl()));
		//配置Server 参数
		TThreadPoolServer.Args arg = new TThreadPoolServer.Args(serverSocket);
		arg.processor(mp);
		arg.protocolFactory(protocolFactory);
		//启动Server
		final TServer server = new TThreadPoolServer(arg);  
		new Thread(
		){public void run() {
			//这个方法会阻塞线程
			server.serve();
		};}.start();
		
		System.out.println("Start server on port " + PORT + " ...");  
	}
}
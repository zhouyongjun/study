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
		//���Ͳ�
		TServerSocket serverSocket = new TServerSocket(PORT);
		//�����
		TMultiplexedProcessor mp = new TMultiplexedProcessor();
		//Э���
		TProtocolFactory protocolFactory = new TCompactProtocol.Factory();  
		mp.registerProcessor("Calculator", new Calculator.Processor<>(new CalculatorImpl()));
		//����Server ����
		TThreadPoolServer.Args arg = new TThreadPoolServer.Args(serverSocket);
		arg.processor(mp);
		arg.protocolFactory(protocolFactory);
		//����Server
		final TServer server = new TThreadPoolServer(arg);  
		new Thread(
		){public void run() {
			//��������������߳�
			server.serve();
		};}.start();
		
		System.out.println("Start server on port " + PORT + " ...");  
	}
}
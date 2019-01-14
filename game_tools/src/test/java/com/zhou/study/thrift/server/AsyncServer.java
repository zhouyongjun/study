package com.zhou.study.thrift.server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

import com.zhou.study.thrift.calc.Calculator;
import com.zhou.study.thrift.calc.CalculatorImpl;

public class AsyncServer {
	public static final int PORT = 9988;
	public static void main(String[] args) {
		try {
			TNonblockingServerSocket socket = new TNonblockingServerSocket(PORT);
			TProcessor processor = new Calculator.Processor<Calculator.Iface>(new CalculatorImpl());
			TProtocolFactory protocolFactory = new TBinaryProtocol.Factory();
			TTransportFactory tfactory = new TFramedTransport.Factory();
			TNonblockingServer.Args arg = new TNonblockingServer.Args(socket);
			arg.processor(processor);
			arg.protocolFactory(protocolFactory);
			arg.transportFactory(tfactory);
			TNonblockingServer server = new TNonblockingServer(arg);
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		
	}
}

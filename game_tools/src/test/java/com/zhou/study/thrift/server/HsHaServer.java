package com.zhou.study.thrift.server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.zhou.study.thrift.calc.Calculator;
import com.zhou.study.thrift.calc.CalculatorImpl;

public class HsHaServer {
	public static final int PORT = 9988;
	public static void main(String[] args) {
		try {
			TNonblockingServerSocket socket = new TNonblockingServerSocket(PORT);
			TProcessor processor = new Calculator.Processor<Calculator.Iface>(new CalculatorImpl());
			THsHaServer.Args arg = new THsHaServer.Args(socket);
			arg.processor(processor);
			arg.protocolFactory(new TBinaryProtocol.Factory());
			arg.transportFactory(new TFramedTransport.Factory());
			THsHaServer server = new THsHaServer(arg);
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		
	}
}

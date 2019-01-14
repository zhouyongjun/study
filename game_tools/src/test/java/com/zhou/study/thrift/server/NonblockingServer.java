package com.zhou.study.thrift.server;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.zhou.study.thrift.calc.Calculator;
import com.zhou.study.thrift.calc.CalculatorImpl;

public class NonblockingServer {
	public static final int PORT = 9988;
	public static void main(String[] args) {
		try {
			TNonblockingServerSocket socket = new TNonblockingServerSocket(PORT);
			TNonblockingServer.Args arg = new TNonblockingServer.Args(socket);
			arg.processor(new Calculator.Processor<Calculator.Iface>(new CalculatorImpl()));
			arg.transportFactory(new TFramedTransport.Factory());
			arg.protocolFactory(new TBinaryProtocol.Factory());
			TNonblockingServer server = new TNonblockingServer(arg);
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
}

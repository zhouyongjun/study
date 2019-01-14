package com.zhou.study.thrift.server;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.zhou.study.thrift.calc.Calculator;
import com.zhou.study.thrift.calc.CalculatorImpl;

public class SimpleServer {
	public static void main(String[] args) {
		TProcessor processor = new Calculator.Processor<Calculator.Iface>(new CalculatorImpl());
		TServerSocket socket;
		int port = 9988;
		try {
			socket = new TServerSocket(port);
			TServer.Args arg = new Args(socket);
			arg.processor(processor);
			arg.protocolFactory(new TCompactProtocol.Factory());
			TServer server = new TSimpleServer(arg);
			server.serve();
			System.out.println("Server port="+port+" is serve.");
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

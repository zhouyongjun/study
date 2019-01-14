package com.zhou.study.thrift.client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import com.zhou.study.thrift.calc.Calculator;

public class HsHaClient {
	public static final int PORT = 9988;
	public static void main(String[] args) {
		TFramedTransport transport = new TFramedTransport(new TSocket("localhost",PORT));
		Calculator.Client client = new Calculator.Client(new TBinaryProtocol(transport));
		try {
			transport.open();
			System.out.println(client.add(1, 2));
			System.out.println(client.multi(1, 2));
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

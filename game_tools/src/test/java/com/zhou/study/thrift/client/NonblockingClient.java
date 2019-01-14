package com.zhou.study.thrift.client;

import java.io.IOException;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

import com.zhou.study.thrift.calc.Calculator;
import com.zhou.study.thrift.calc.Calculator.Client;

public class NonblockingClient {
	public static final int PORT = 9988;
	public static void main(String[] args) {
		try {
			TFramedTransport transport = new TFramedTransport(new TSocket("localhost", PORT));
			Calculator.Client client = new Client(new TBinaryProtocol(transport));
			transport.open();
			System.out.println(client.add(1, 2));
			System.out.println(client.multi(1, 2));
			transport.close();
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}finally
		{
		}
		
	}
}

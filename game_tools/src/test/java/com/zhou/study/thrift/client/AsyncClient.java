package com.zhou.study.thrift.client;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

import com.zhou.study.thrift.calc.Calculator;

public class AsyncClient {
	public static final int PORT = 9988;
	public static void main(String[] args) {
		
		TNonblockingTransport transport;
		try {
			TAsyncClientManager manager = new TAsyncClientManager();
			transport = new TNonblockingSocket("localhost",PORT);
			TProtocolFactory pfactory = new TBinaryProtocol.Factory();
			Calculator.AsyncClient client = new Calculator.AsyncClient(pfactory,manager,transport);
			System.out.println("Client start .....");
			CountDownLatch latch = new CountDownLatch(1);
//			AsynCallback callback = ;
			System.out.println("call method add start ...");
			client.add(1, 1, new AsynCallback(latch));
			System.out.println("call method add .... end");
			boolean wait = latch.await(30, TimeUnit.SECONDS);
			System.out.println("latch.await =:" + wait);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("startClient end.");
	}
	public static class AsynCallback<TAsyncMethodCall> implements  AsyncMethodCallback<TAsyncMethodCall>{
		CountDownLatch latch;
		public AsynCallback(CountDownLatch latch) {
			this.latch = latch;
		}
		@Override
		public void onComplete(TAsyncMethodCall response) {
			System.out.println("onComplete");
			try {
				// Thread.sleep(1000L * 1);
				System.out.println("AsynCall result =:"
						+ response);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}

		@Override
		public void onError(Exception exception) {
			System.out.println("onError :" + exception.getMessage());
			latch.countDown();
			
		}
		

	}
}

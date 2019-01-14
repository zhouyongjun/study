package com.zhou.study.nio.test.blocking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BlockingSocketMain {
	static volatile int begin_count = 0;
	static volatile int over_count = 0;
	static String[] HOSTS = {"www.baidu.com", "www.weibo.com", "www.sina.com"};
	static int PORT = 80;
	private static final Logger logger = LogManager.getLogger();
	public static void main(String[] args) {
		for (int i=0;i<10000;i++)
		{
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						SocketChannel channel = SocketChannel.open();
						channel.configureBlocking(true);
						channel.socket().setSoTimeout(50000);
						SocketAddress address = new InetSocketAddress(HOSTS[1], PORT);
						channel.connect(address);
						BufferedReader br = new BufferedReader(new InputStreamReader(channel.socket().getInputStream()));
						PrintWriter pw = new PrintWriter(channel.socket().getOutputStream());
						pw.write(compositeRequest(HOSTS[1]));
						pw.flush();
						String readline = null;
						while ((readline=br.readLine()) != null)
						{
//							System.out.println(readline);
						}
						over_count ++;
						logger.info("run: begin_count="+begin_count+"  over_count="+over_count);
					} catch (SocketException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			begin_count++;
			logger.info("start: begin_count="+begin_count+"  over_count="+over_count);
		}
	}
	public static String compositeRequest(String host){

        return "GET / HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "User-Agent: curl/7.43.0\r\n" +
                "Accept: */*\r\n\r\n";
    }
}

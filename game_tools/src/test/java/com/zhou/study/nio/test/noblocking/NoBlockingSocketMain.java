package com.zhou.study.nio.test.noblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NoBlockingSocketMain {
	static volatile int begin_count = 0;
	static volatile int write_count = 0;
	static volatile int receive_count = 0;
	static String[] HOSTS = {"www.baidu.com", "www.weibo.com", "www.sina.com"};
	static int PORT = 80;
	static Charset charset = Charset.forName("utf-8");
	private static final Logger logger = LogManager.getLogger();
	static Selector selector;
	public static void main(String[] args) {
		try {
			selector = Selector.open();
			for (int i=0;i<1;i++)
			{
				init(i);				
			}
			select();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void init(int i) throws IOException {
		SocketChannel channel = SocketChannel.open();
		SocketAddress remote = new InetSocketAddress(HOSTS[i],PORT);
		channel.configureBlocking(false);
		channel.connect(remote);
		channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE);		
	}

	 
	
	private static void select() {
		/*
		 * 这里的selector.select()是同步阻塞的，等待有事件发生后，才会被唤醒。这就防止了 CPU 空转的产生。当然，我们也可以给它设置超时时间，selector.select(long timeout)来结束阻塞过程。
		 */
		
		try {
			while (selector.select() > 0)
			{
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext())
				{
					SelectionKey key  = iterator.next();
					iterator.remove();
					logger.debug("select : " + key.readyOps());
					if (key.isConnectable())
					{
						connect(key);
					}
					 if (key.isReadable())
					{
						receive(key);
					}
					 if(key.isWritable())
					{
						write(key);
					}
				Thread.sleep(1000);
				}
//				for (SelectionKey key : keys)
//				{
//					if (key.isConnectable())
//					{
//						connect(key);
//					}
//					else if (key.isReadable())
//					{
//						receive(key);
//					}
//					else if(key.isWritable())
//					{
//						write(key);
//					}
//				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void write(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
//		BufferedReader br = new BufferedReader(new InputStreamReader(channel.socket().getInputStream()));
		InetSocketAddress remote = (InetSocketAddress) channel.socket().getRemoteSocketAddress();
        String host = remote.getHostName();
		channel.write(charset.encode(compositeRequest(host)));
		System.out.println("write:" +host);
//		key.interestOps(SelectionKey.OP_READ);
	}
	private static void receive(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		InetSocketAddress remote = (InetSocketAddress) channel.socket().getRemoteSocketAddress();
        String host = remote.getHostName();
        System.out.println("receive:" +host);        
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		channel.read(buffer);
		buffer.flip();
		String msg = charset.decode(buffer).toString();
		System.out.println(msg);
		if ("".equals(""))
		{
//			key.cancel();
//			channel.close();
			return;
		}
	}
	private static void connect(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			channel.finishConnect();
			InetSocketAddress remote = (InetSocketAddress) channel.socket().getRemoteSocketAddress();
			 System.out.println(String.format("访问地址: %s:%s 连接成功!", remote.getHostName(), remote.getPort()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static String compositeRequest(String host){

        return "GET / HTTP/1.1\r\n" +
                "Host: " + host + "\r\n" +
                "User-Agent: curl/7.43.0\r\n" +
                "Accept: */*\r\n\r\n";
    }
}

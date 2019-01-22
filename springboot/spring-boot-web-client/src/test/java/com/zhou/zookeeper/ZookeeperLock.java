package com.zhou.zookeeper;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * zookeeper 实现：分布式锁
 * @author zhouyongjun
 *
 */
public class ZookeeperLock {
	String connectString = "127.0.0.1:2181";
	int sessionTimeout = 5000;
	String lockNameSpace = "/mylock";
	String nodeString = lockNameSpace+"/lock1";
	ZooKeeper zk;
	Logger logger = LoggerFactory.getLogger(ZookeeperLock.class);
	public ZookeeperLock() {
		try {
			zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					logger.info("Recive WatchedEvent : " + event);
					if (event.getState() == Event.KeeperState.SyncConnected)
					{
						logger.info("Connection successful. ");
						System.out.println("connection is established...");
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 确保跟路径OK
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * @throws Exception
	 */
	public void ensureRootPath() throws  InterruptedException
	{
		try {
			if (zk.exists(lockNameSpace, true) == null)
			{
				zk.create(lockNameSpace, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			}
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ExecutorService service = Executors.newFixedThreadPool(10);
		for (int i=0;i<4;i++)
		{
			service.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						//创建对象zookeeper
						ZookeeperLock zl = new ZookeeperLock();
						// 路径确认
						/*
						 * 获取锁
						 *  没锁->创建锁
						 *  有锁没使用->直接获取
						 *  有锁在使用中->等待锁释放
						 */
						zl.lock();
						Thread.sleep(3000);
						zl.unlock();
						//释放锁
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
		
		service.shutdown();
	}

	private void unlock() {
		try {
			zk.delete(nodeString, -1);
			  System.out.println("Thread.currentThread().getName() +  release Lock...");
		} catch (InterruptedException | KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean lock() throws InterruptedException{
		String path = null;
		ensureRootPath();
		watchNode(nodeString,Thread.currentThread());
		while(true)
		{
			try {
				path = zk.create(nodeString, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
			} catch (KeeperException e) {
//				e.printStackTrace();
				  System.out.println(Thread.currentThread().getName() + "  getting Lock but can not get");
				try {
					Thread.sleep(5000);
				} catch (Exception e1) {
					 System.out.println("thread is notify");
				}
			}
			if (!StringUtils.isBlank(path))
			{
				 System.out.println(Thread.currentThread().getName() + "  get Lock...");
				return true;
			}
		}
	}

	public void watchNode(String nodeString, Thread thread) throws InterruptedException {
		try {
			zk.exists(nodeString, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					System.out.println( "==" + event.toString());
					if (event.getType() == Event.EventType.NodeDeleted)
					{
						 System.out.println("Threre is a Thread released Lock==============");
						thread.interrupt();
					}
					try {
						zk.exists(nodeString, new Watcher() {
							@Override
							public void process(WatchedEvent event) {
								System.out.println( "==" + event.toString());
								if (event.getType() == Event.EventType.NodeDeleted)
								{
									 System.out.println("Threre is a Thread released Lock==============");
									thread.interrupt();
								}
								try {
									zk.exists(nodeString, true);
								} catch (KeeperException | InterruptedException e) {
									e.printStackTrace();
								}
							}
						});
					} catch (KeeperException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (KeeperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

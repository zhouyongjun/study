package com.zhou.test;

import java.nio.ByteBuffer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectByteBufferTest {
	final Logger log = LoggerFactory.getLogger(DirectByteBufferTest.class);
	@Test
	public void test()
	{

		int _lG = 1024 * 1024 * 1024;
		 String valuel = "test mem。 ry ";
		log.info ("before freeMemory :"+ Runtime.getRuntime().freeMemory ());
		/*
		＊直接在物理内存中分配一块固定大小的直接字节缓冲区*/
		ByteBuffer buffer = ByteBuffer.allocate(_lG);
		log.info("after freeMemory:"+ Runtime. getRuntime ().freeMemory ());
		/*写人数据＊*/
		buffer.put(valuel.getBytes());
		/*切换为读模式*/
		buffer.flip() ;
		log. info ("从物理内存中读出数据一〉");
		for (int i= 0; i < valuel.length();i++) {
		/*不影响指针的偏移量*/
		log.info((char) buffer.get(i)+"") ;
		byte[] value2 =new byte[valuel.length()];
		buffer.get(value2, 0, value2.length);
		log.info("从物理内存中读出数据一〉" + new String(value2));
	}

	}
}

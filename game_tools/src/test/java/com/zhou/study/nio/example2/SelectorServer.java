package com.zhou.study.nio.example2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 58 on 2016/11/28.
 */
public class SelectorServer {
    public static void main(String args[]){
        startServer();
    }
    public static ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
    public static ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

    public static void startServer(){
        //用两个channel监听两个端口
        int listenPort = 8888;
        int listenPort1 = 8889;
        sendBuffer.put("message from server".getBytes());

        try {
            //创建serverchannel，绑定对应的端口
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(listenPort);
            serverSocket.bind(inetSocketAddress);

            //创建第二个channel
            ServerSocketChannel serverSocketChannel1 = ServerSocketChannel.open();
            ServerSocket serverSocket1 = serverSocketChannel1.socket();
            InetSocketAddress inetSocketAddress1 = new InetSocketAddress(listenPort1);
            serverSocket1.bind(inetSocketAddress1);


            //创建selector对象
            Selector selector = Selector.open();

            //设置channel注册到selector中
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //第二个channel
            serverSocketChannel1.configureBlocking(false);
            serverSocketChannel1.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("start to listen port: " + listenPort);
            System.out.println("start to listen port: " + listenPort1);

            //监听端口
            while(true){
                int readyChannels = selector.select();
                if(readyChannels == 0)
                    continue;
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    dealSelectionKey(selector, selectionKey);

                    iterator.remove();
                }//while
            }//while


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dealSelectionKey(Selector selector, SelectionKey selectionKey){
        try{
            //准备好接收新的连接
            if(selectionKey.isAcceptable()){
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel clientSocketChannel = serverSocketChannel.accept();
                clientSocketChannel.configureBlocking(false);
                clientSocketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                System.out.println("channel is ready acceptable");
            }
            else if(selectionKey.isConnectable()){
                selectionKey.channel().register(selector, SelectionKey.OP_READ);
                System.out.println("channel is connectable.");
            }
            else if(selectionKey.isReadable()){
                //读去客户端内容
                SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
                receiveBuffer.clear();
                clientSocketChannel.read(receiveBuffer);
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                System.out.println("message from client is: " + new String(receiveBuffer.array()));
                System.out.println("Thread id : " + Thread.currentThread().getId());
            }
            else if(selectionKey.isWritable()){
                //向客户端写数据
                SocketChannel clientSocketChannel = (SocketChannel) selectionKey.channel();
                sendBuffer.flip();
                System.out.println("sendBuffer = " + new String(sendBuffer.array()));
                clientSocketChannel.write(sendBuffer);
                selectionKey.interestOps(SelectionKey.OP_READ);
                System.out.println("channle is writable.");
            }//else if
        }catch (Exception e){

        }
    }
}
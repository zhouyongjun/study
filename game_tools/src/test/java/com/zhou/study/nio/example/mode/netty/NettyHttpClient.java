package com.zhou.study.nio.example.mode.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.zhou.study.nio.example.constant.HttpConstant;

/**
 * Created by jason-geng on 8/16/17.
 */
public class NettyHttpClient {

    public static void main(String[] args) throws InterruptedException {
        new NettyHttpClient().start();
    }

    public void start() throws InterruptedException {
        Bootstrap b = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new HttpClientChannelInitializer());

        for (String host : HttpConstant.HOSTS) {
            ChannelFuture cf = b.connect(host, HttpConstant.PORT).sync();
            cf.channel().closeFuture().sync();
        }

    }

}

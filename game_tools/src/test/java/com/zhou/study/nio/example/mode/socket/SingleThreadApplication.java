package com.zhou.study.nio.example.mode.socket;

import com.zhou.study.nio.example.constant.HttpConstant;

/**
 * Created by jason-geng on 8/16/17.
 */
public class SingleThreadApplication {

    public static void main(String[] args) {

        for (final String host: HttpConstant.HOSTS) {

            new SocketHttpClient().start(host, HttpConstant.PORT);

        }

    }
}

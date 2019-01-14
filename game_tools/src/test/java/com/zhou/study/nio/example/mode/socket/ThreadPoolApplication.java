package com.zhou.study.nio.example.mode.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zhou.study.nio.example.constant.HttpConstant;

/**
 * Created by jason-geng on 8/16/17.
 */
public class ThreadPoolApplication {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        for (final String host: HttpConstant.HOSTS) {

            Thread t = new Thread(new Runnable() {
                public void run() {
                    new SocketHttpClient().start(host, HttpConstant.PORT);
                }
            });

            executorService.submit(t);
            new SocketHttpClient().start(host, HttpConstant.PORT);

        }

    }
}

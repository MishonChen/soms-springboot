package xyz.ontip.single;

import nonapi.io.github.classgraph.concurrency.SingletonMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TokenBlockListSingletonList {
    private static volatile List<Object> instance;

    private TokenBlockListSingletonList(){

    }

    public static List<Object> getInstance(){
        // 第一次检查，如果instance不为null，则直接返回
        if (instance == null) {
            // 同步代码块，确保只有一个线程能进入
            synchronized (SingletonMap.class) {
                // 第二次检查，防止多个线程同时进入同步代码块
                if (instance == null) {
                    instance = Collections.synchronizedList(new ArrayList<>());
                }
            }
        }
        return instance;
    }
}

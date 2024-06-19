package xyz.ontip.single;

import nonapi.io.github.classgraph.concurrency.SingletonMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public  class TokenBlockListSingletonMap {
    // volatile关键字确保多线程环境下的可见性
    private static volatile Map<String, Object> instance;

    // 私有构造函数，防止外部通过new创建实例
    private TokenBlockListSingletonMap(){

    }
    // 提供全局访问点
    public static Map<String, Object> getInstance() {
        // 第一次检查，如果instance不为null，则直接返回
        if (instance == null) {
            // 同步代码块，确保只有一个线程能进入
            synchronized (SingletonMap.class) {
                // 第二次检查，防止多个线程同时进入同步代码块
                if (instance == null) {
                    instance = Collections.synchronizedMap(new HashMap<>());
                }
            }
        }
        return instance;
    }

}

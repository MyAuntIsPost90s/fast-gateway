package com.ch.web.gateway.session;

import com.ch.web.gateway.token.TokenGenerateStrategy;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户session创建者
 *
 * @author caich
 **/
public class UserSessionBuilder {

    private static volatile SoftReference<ConcurrentHashMap<String, TokenGenerateStrategy>> strategyCache = new SoftReference<>(new ConcurrentHashMap<>());

    /**
     * 构造会话
     *
     * @param data          用户数据
     * @param strategyClass 策略类
     * @return
     **/
    public static <T> UserSession<T> build(String sessionId, T data, Class<? extends TokenGenerateStrategy> strategyClass) throws Exception {
        TokenGenerateStrategy<T> strategy = getStrategyByCacheFirst(strategyClass);
        String accessToken = strategy.generate(data);
        return new UserSession<>(accessToken, sessionId, data);
    }

    /**
     * 从缓存种获取策略实现
     *
     * @param strategyClass 策略类
     * @return
     **/
    private static <T> TokenGenerateStrategy<T> getStrategyByCacheFirst(Class<? extends TokenGenerateStrategy> strategyClass) throws Exception {
        ConcurrentHashMap<String, TokenGenerateStrategy> strategyMap = strategyCache.get();   // 转为强引用
        if (strategyMap == null) {
            synchronized (UserSessionBuilder.class) {
                strategyMap = strategyCache.get();
                if (strategyMap == null) {
                    strategyMap = new ConcurrentHashMap<>();
                    strategyCache = new SoftReference<>(strategyMap);
                }
            }
        }
        TokenGenerateStrategy strategy = strategyMap.get(strategyClass.getName());
        if (strategy == null) { // 找不到策略则新增
            strategy = strategyClass.newInstance();
            strategyMap.put(strategyClass.getName(), strategy);
        }
        return strategy;
    }

}

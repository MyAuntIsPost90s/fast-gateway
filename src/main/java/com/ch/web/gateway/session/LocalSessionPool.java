package com.ch.web.gateway.session;

import com.ch.web.gateway.util.GatewayLogUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 本地会话池 （利用guava做简单的缓存处理）
 *
 * @author caich
 **/
public class LocalSessionPool extends BaseSessionPool {

    private boolean allowRepeatSession = true;

    private long sessionLiveTime = 1000L * 60 * 60 * 2;  // 默认2小时

    private long maxSessionSize = 10000L * 1; // 默认1w

    private static Cache<String, UserSession> sessions; // 会话本地缓存

    private final static ConcurrentHashMap<String, String> sessionIdMap = new ConcurrentHashMap<>();    // 已存在的会话记录

    public LocalSessionPool() {
        initSessionPool();
    }

    public LocalSessionPool(boolean allowRepeatSession, long sessionLiveTime, long maxSessionSize) {
        this.allowRepeatSession = allowRepeatSession;
        this.sessionLiveTime = sessionLiveTime;
        this.maxSessionSize = maxSessionSize;
        initSessionPool();
    }

    @Override
    public UserSession get(String accessToken) {
        return sessions.getIfPresent(accessToken);
    }

    @Override
    public void add(UserSession session) {
        if (!this.allowRepeatSession) { // 相同会话顶替
            synchronized (sessionIdMap) {
                if (sessionIdMap.containsKey(session.getSessionId())) {
                    String accessToken = sessionIdMap.get(session.getSessionId());
                    delete(accessToken);
                    sessions.put(session.getAccessToken(), session);
                } else {
                    sessions.put(session.getAccessToken(), session);
                }
                sessionIdMap.put(session.getSessionId(), session.getAccessToken());
            }
        } else {    // 允许重复创建相同会话
            sessions.put(session.getAccessToken(), session);
        }

    }

    @Override
    public void update(UserSession session) {
        delete(session.getAccessToken());
        add(session);
    }

    @Override
    public void delete(String accessToken) {
        sessions.invalidate(accessToken);
    }

    /**
     * 初始化会话池
     *
     * @return void
     **/
    private void initSessionPool() {
        sessions = CacheBuilder.newBuilder()
                .maximumSize(this.maxSessionSize) // 设置缓存的最大容量
                .expireAfterAccess(this.sessionLiveTime, TimeUnit.MILLISECONDS) // 设置缓存过期时间
                .concurrencyLevel(5) // 设置5个区块同时操作
                .removalListener((notification) -> {
                    if (!this.allowRepeatSession) {
                        UserSession session = (UserSession) notification.getValue();
                        sessionIdMap.remove(session.getSessionId());
                    }
                    GatewayLogUtil.debug("Clean Token:" + notification.getKey());
                })
                .build();
    }
}

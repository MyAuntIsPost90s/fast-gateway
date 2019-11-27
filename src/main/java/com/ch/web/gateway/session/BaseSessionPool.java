package com.ch.web.gateway.session;

/**
 * 抽象会话池
 *
 * @author caich
 */
public abstract class BaseSessionPool {

    /**
     * 通过Token获取tokenBase对象
     *
     * @param accessToken
     * @return
     */
    public abstract UserSession get(String accessToken);

    /**
     * 插入
     *
     * @param session
     */
    public abstract void add(UserSession session);

    /**
     * 修改
     *
     * @param session
     */
    public abstract void update(UserSession session);

    /**
     * 移除
     *
     * @param accessToken
     */
    public abstract void delete(String accessToken);
}

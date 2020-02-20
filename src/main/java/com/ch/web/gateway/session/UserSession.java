package com.ch.web.gateway.session;

import java.io.Serializable;

/**
 * 用户会话
 *
 * @author caich
 **/
public class UserSession<T> implements Serializable {

    /**
     * 会话id 用于确定数据唯一性
     */
    private String sessionId;

    /**
     * 令牌
     */
    private String accessToken;

    /**
     * 会话数据
     */
    private T data;

    public UserSession(String accessToken, String sessionId, T data) {
        this.accessToken = accessToken;
        this.sessionId = sessionId;
        this.data = data;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public T getData() {
        return data;
    }

    public String getSessionId() {
        return sessionId;
    }
}

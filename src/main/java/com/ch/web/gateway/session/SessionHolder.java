package com.ch.web.gateway.session;

import com.ch.web.gateway.request.RequestHolder;
import com.ch.web.gateway.response.ResponseHolder;
import com.ch.web.gateway.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 会话句柄
 *
 * @author caich
 **/
public class SessionHolder {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private volatile RequestHolder requestHolder;

    private volatile ResponseHolder responseHolder;

    private String accessToken;

    private SessionHolder(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.accessToken = RequestUtil.getAccessToken(request);
    }

    public static SessionHolder get(HttpServletRequest request, HttpServletResponse response) {
        if (request == null) {
            throw new IllegalArgumentException("request not set null");
        }
        if (response == null) {
            throw new IllegalArgumentException("response not set null");
        }
        return new SessionHolder(request, response);
    }

    public <T> UserSession<T> getUserSession() {
        if (accessToken == null || accessToken.isEmpty()) {
            return null;
        }
        return UserSessionManager.getInstance().getSession(this.accessToken);
    }

    public RequestHolder getRequestHolder() {
        if (requestHolder == null) {
            synchronized (this) {
                if (requestHolder == null) {
                    requestHolder = RequestHolder.get(request);
                }
            }
        }
        return requestHolder;
    }

    public ResponseHolder getResponseHolder() {
        if (responseHolder == null) {
            synchronized (this) {
                if (responseHolder == null) {
                    responseHolder = ResponseHolder.get(response);
                }
            }
        }
        return responseHolder;
    }
}

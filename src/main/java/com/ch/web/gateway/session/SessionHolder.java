package com.ch.web.gateway.session;

import com.ch.web.gateway.request.RequestEnums;
import com.ch.web.gateway.request.RequestFile;
import com.ch.web.gateway.request.RequestHolder;
import com.ch.web.gateway.response.ResponseHolder;
import com.ch.web.gateway.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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

    private static volatile ThreadLocal<SessionHolder> threadLocal;

    static {
        threadLocal = new ThreadLocal<>();
    }

    private SessionHolder(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.accessToken = RequestUtil.getAccessToken(request);
    }

    public static void loadSessionHolder(HttpServletRequest request, HttpServletResponse response) {
        if (request == null) {
            throw new IllegalArgumentException("request not set null");
        }
        if (response == null) {
            throw new IllegalArgumentException("response not set null");
        }
        SessionHolder sessionHolder = new SessionHolder(request, response);
        synchronized (threadLocal) { // threadLocal.set 存在 hash table resize
            threadLocal.set(sessionHolder);
        }
    }

    public static SessionHolder currentSessionHolder() {
        return threadLocal.get();
    }

    public <T> UserSession<T> getUserSession() {
        if (accessToken == null || accessToken.isEmpty()) {
            return null;
        }
        return UserSessionManager.get().getSession(this.accessToken);
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

    public <T> T getParam(String paramName, Class<T> objectClass) {
        return getRequestHolder().getParam(paramName, objectClass);
    }

    public <T> T getObjectParam(Class<T> objectClass, RequestEnums.ParamType paramType) {
        return getRequestHolder().getObjectParam(objectClass, paramType);
    }

    public <T> List<T> getArrayParamByJson(Class<T> objectClass) {
        return getRequestHolder().getArrayParamByJson(objectClass);
    }

    public RequestFile getRequestFile() {
        return getRequestHolder().getRequestFile();
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

    public void success(Object data) {
        getResponseHolder().success(data);
    }

    public void success(String msg) {
        getResponseHolder().success(msg);
    }

    public void success(String msg, Object data) {
        getResponseHolder().success(msg, data);
    }

    public void success(String msg, String msgCode) {
        getResponseHolder().success(msg, msgCode);
    }

    public void success(Object data, String msgCode) {
        getResponseHolder().success(data, msgCode);
    }

    public void success(String msg, Object data, String msgCode) {
        getResponseHolder().success(msg, data, msgCode);
    }

    public void fail(Object data) {
        getResponseHolder().fail(data);
    }

    public void fail(String msg) {
        getResponseHolder().fail(msg);
    }

    public void fail(String msg, String msgCode) {
        getResponseHolder().fail(msg, msgCode);
    }

    public void fail(Object data, String msgCode) {
        getResponseHolder().fail(data, msgCode);
    }

    public void fail(String msg, Object data) {
        getResponseHolder().fail(msg, data, null);
    }

    public void fail(String msg, Object data, String msgCode) {
        getResponseHolder().fail(msg, data, msgCode);
    }

    public void entity(Object data) {
        getResponseHolder().entity(data);
    }
}

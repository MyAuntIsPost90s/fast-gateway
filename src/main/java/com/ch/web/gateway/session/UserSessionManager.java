package com.ch.web.gateway.session;

import com.ch.web.gateway.boot.FastGatewayContext;

/**
 * 会话统管
 *
 * @author caich
 **/
public class UserSessionManager {

    private static volatile UserSessionManager manager;

    private UserSessionManager() {

    }

    public static UserSessionManager get() {
        if (manager == null) {
            synchronized (UserSessionManager.class) {
                if (manager == null) {
                    manager = new UserSessionManager();
                }
            }
        }
        return manager;
    }

    public void addSession(UserSession userSession) {
        FastGatewayContext.getCurrentContext().getSessionPool().add(userSession);
    }

    public void updateSession(UserSession userSession) {
        FastGatewayContext.getCurrentContext().getSessionPool().update(userSession);
    }

    public void removeSession(String accessToken) {
        FastGatewayContext.getCurrentContext().getSessionPool().delete(accessToken);
    }

    public <T> UserSession<T> getSession(String accessToken) {
        return FastGatewayContext.getCurrentContext().getSessionPool().get(accessToken);
    }
}

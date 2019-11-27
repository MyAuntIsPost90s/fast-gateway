package com.ch.web.gateway.boot;

import com.ch.web.gateway.config.FastGatewayConfig;
import com.ch.web.gateway.session.BaseSessionPool;
import org.springframework.context.ApplicationContext;

/**
 * 网关上下文
 *
 * @author caich
 **/
public class FastGatewayContext extends FastGatewayConfig {

    protected ApplicationContext applicationContext; // spring上下文

    protected FastGatewayConfig fastGatewayConfig;   // 网关配置

    protected BaseSessionPool sessionPool;   // 会话池

    protected static FastGatewayContext fastGatewayContext; // 配置的上下文

    protected FastGatewayContext() {
    }

    public static FastGatewayContext getCurrentContext() {
        if (fastGatewayContext == null) {
            throw new IllegalArgumentException("[Error]boot not run; fastGatewayContext is null.");
        }
        return fastGatewayContext;
    }

    public ApplicationContext getApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalArgumentException("[Error]applicationContext not set。");
        }
        return applicationContext;
    }

    public FastGatewayConfig getFastGatewayConfig() {
        if (fastGatewayConfig == null) {
            throw new IllegalArgumentException("[Error]fastGatewayConfig not set。");
        }
        return fastGatewayConfig;
    }

    public BaseSessionPool getSessionPool() {
        if (sessionPool == null) {
            throw new IllegalArgumentException("[Error]boot not register SessionPool, you can use default pool[com.ch.web.gateway.session.LocalSessionPool] or extends com.ch.web.gateway.session.BaseSessionPool by yourself");
        }
        return sessionPool;
    }
}

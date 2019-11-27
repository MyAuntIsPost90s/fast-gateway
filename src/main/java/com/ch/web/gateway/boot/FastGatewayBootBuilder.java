package com.ch.web.gateway.boot;

import com.ch.web.gateway.session.BaseSessionPool;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;

/**
 * 引导类建造者
 *
 * @author caich
 **/
public class FastGatewayBootBuilder {

    private FastGatewayBoot fastGatewayBoot;

    private FastGatewayBootBuilder(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            throw new IllegalArgumentException("applicationContext can't set null");
        }
        fastGatewayBoot = new FastGatewayBoot(applicationContext);
    }

    /**
     * 构造引导类
     *
     * @param
     * @return
     **/
    public static synchronized FastGatewayBootBuilder newBuilder(ApplicationContext applicationContext) {
        return new FastGatewayBootBuilder(applicationContext);
    }

    /**
     * 设置编码类型
     *
     * @param encode 编码类型
     * @return
     **/
    public FastGatewayBootBuilder setEncode(String encode) {
        fastGatewayBoot.setEncode(encode);
        return this;
    }

    /**
     * 是否允许重复登陆
     *
     * @param allowCross
     * @return
     **/
    public FastGatewayBootBuilder allowCross(boolean allowCross) {
        fastGatewayBoot.setAllowCross(allowCross);
        return this;
    }

    /**
     * 设置过滤器
     *
     * @param interceptorRegistration 拦截器配置
     * @return
     **/
    public FastGatewayBootBuilder registerInterceptor(InterceptorRegistration interceptorRegistration) {
        if (interceptorRegistration != null) {
            fastGatewayBoot.getInterceptors().add(interceptorRegistration);
        }
        return this;
    }

    /**
     * 注册会话池
     *
     * @param
     * @return
     **/
    public FastGatewayBootBuilder registerSessionPool(BaseSessionPool sessionPool) {
        if (sessionPool == null) {
            throw new IllegalArgumentException("sessionPool can't set null");
        }
        fastGatewayBoot.setSessionPool(sessionPool);
        return this;
    }

    /**
     * 添加引导扩展
     *
     * @param
     * @return
     **/
    public <T extends FastGatewayBootExtend> FastGatewayBootBuilder addBootExtend(Class<T> bootClass) {
        try {
            fastGatewayBoot.getBootExtends().add(bootClass.newInstance());
        } catch (Exception e) {
            throw new IllegalArgumentException("AddBootExtend Error", e);
        }
        return this;
    }

    /**
     * 建造
     *
     * @return FastGatewayBoot
     **/
    public FastGatewayBoot build() {
        return fastGatewayBoot;
    }
}

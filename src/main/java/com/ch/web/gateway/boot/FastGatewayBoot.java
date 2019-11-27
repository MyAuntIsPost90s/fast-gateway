package com.ch.web.gateway.boot;

import com.ch.web.gateway.interceptor.BaseInterceptor;
import com.ch.web.gateway.session.BaseSessionPool;
import com.ch.web.gateway.util.InterceptorRegistryUtil;
import com.google.common.collect.ImmutableList;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.ArrayList;
import java.util.List;

public class FastGatewayBoot extends FastGatewayContext {

    private List<FastGatewayBootExtend> bootExtends;
    private List<InterceptorRegistration> interceptorRegistrations;

    protected FastGatewayBoot(ApplicationContext applicationContext) {
        this.bootExtends = new ArrayList<>();
        this.interceptorRegistrations = new ArrayList<>();
        this.applicationContext = applicationContext;
        this.fastGatewayConfig = this;
    }

    /**
     * 启动
     *
     * @param
     * @return
     **/
    public FastGatewayBoot start() {
        fixedInterceptors();
        setFastGatewayContext();
        executeBootExtends();
        return this;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        try {
            InterceptorRegistryUtil.registry(registry, interceptorRegistrations);
        } catch (Exception e) {
            throw new IllegalStateException("[Error] Register Interceptor error", e);
        }
    }

    protected void setEncode(String encode) {
        super.encode = encode;
    }

    protected List<InterceptorRegistration> getInterceptors() {
        return interceptorRegistrations;
    }

    protected void setAllowCross(boolean allowCross) {
        this.allowCross = allowCross;
    }

    protected void setSessionPool(BaseSessionPool sessionPool) {
        super.sessionPool = sessionPool;
    }

    protected List<FastGatewayBootExtend> getBootExtends() {
        return this.bootExtends;
    }

    private void setFastGatewayContext() {
        this.fastGatewayContext = this;
    }

    private void fixedInterceptors() {
        interceptorRegistrations.add(0, new InterceptorRegistration(new BaseInterceptor()).addPathPatterns("/**"));
        this.interceptorRegistrations = ImmutableList.copyOf(interceptorRegistrations); // 禁止更改集合
    }

    private void executeBootExtends() {
        this.bootExtends = ImmutableList.copyOf(bootExtends); // 禁止更改集合
        this.bootExtends.forEach(o -> o.execute());
    }
}

package com.ch.web.gateway.util;

import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 拦截器注册帮助类
 *
 * @author caich
 **/
public class InterceptorRegistryUtil {

    public static void registry(InterceptorRegistry registry, List<InterceptorRegistration> registrations) throws Exception {
        Field field = registry.getClass().getDeclaredField("registrations");
        field.setAccessible(true);
        ((List<InterceptorRegistration>) field.get(registry)).addAll(registrations);
    }
}

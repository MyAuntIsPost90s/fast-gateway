package com.ch.web.gateway.interceptor;

import com.ch.web.gateway.boot.FastGatewayContext;
import com.ch.web.gateway.util.GatewayLogUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 编码拦截器
 *
 * @author caich
 **/
public class EncodeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String charset = FastGatewayContext.getCurrentContext().getFastGatewayConfig().getEncode();
        try {
            request.setCharacterEncoding(charset);
        } catch (Exception e) {
            GatewayLogUtil.error(e);
        }
        response.setCharacterEncoding(charset);
        response.setContentType("text/html;charset=" + charset);
        return true;
    }
}

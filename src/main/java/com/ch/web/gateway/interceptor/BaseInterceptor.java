package com.ch.web.gateway.interceptor;

import com.ch.web.gateway.boot.FastGatewayContext;
import com.ch.web.gateway.response.ResponseHolder;
import com.ch.web.gateway.session.SessionHolder;
import com.ch.web.gateway.session.UserSession;
import com.ch.web.gateway.util.RequestUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FastGateway基础拦截器（默认使用）
 *
 * @author caich
 **/
public class BaseInterceptor implements HandlerInterceptor {

    private static String OPTIONS = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        FastGatewayContext fastGatewayContext = FastGatewayContext.getCurrentContext();
        if (fastGatewayContext.getFastGatewayConfig().getAllowCross()) {    // 允许跨域的情况下支持option请求
            setCrossHeader(request, response);
            boolean isOption = request.getMethod().toUpperCase().equals(OPTIONS);
            if (isOption) {
                return false;
            }
        }
        setSessionHolder(request, response);
        return true;
    }

    private void setCrossHeader(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (origin != null && !origin.isEmpty()) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "0");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, AppKey, AccessToken");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
    }

    private void setSessionHolder(HttpServletRequest request, HttpServletResponse response) {
        SessionHolder.loadSessionHolder(request, response);
    }
}

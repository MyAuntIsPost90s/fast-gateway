package com.ch.web.gateway.interceptor;

import com.ch.web.gateway.boot.FastGatewayContext;
import com.ch.web.gateway.response.ResponseHolder;
import com.ch.web.gateway.session.UserSession;
import com.ch.web.gateway.util.RequestUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 *
 * @author caich
 **/
public class AuthorityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!checkAuthority(request, response)) {  // 校验权限
            return false;
        }
        return true;
    }

    private boolean checkAuthority(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = RequestUtil.getAccessToken(request);
        ResponseHolder responseHolder = ResponseHolder.get(response);
        if (accessToken == null || accessToken.isEmpty()) {
            responseHolder.fail(AuthorityEnum.AuthoritySate.ILLEGAL.msg, AuthorityEnum.AuthoritySate.ILLEGAL.msgCode);
            return false;
        }
        FastGatewayContext fastGatewayContext = FastGatewayContext.getCurrentContext();
        UserSession userSession = fastGatewayContext.getSessionPool().get(accessToken);
        if (userSession == null) {
            responseHolder.fail(AuthorityEnum.AuthoritySate.EXPIRED.msg, AuthorityEnum.AuthoritySate.EXPIRED.msgCode);
            return false;
        }
        return true;
    }
}

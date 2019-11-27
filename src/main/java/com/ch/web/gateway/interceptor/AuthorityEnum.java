package com.ch.web.gateway.interceptor;

/**
 * 权限枚举
 *
 * @author caich
 **/
public class AuthorityEnum {

    /**
     * 权限状态
     *
     * @author caich
     **/
    public enum AuthoritySate {
        ILLEGAL("0x0000", "非法请求"), EXPIRED("0x0001", "会话失效，请重新登陆");
        public String msgCode;
        public String msg;

        AuthoritySate(String msgCode, String msg) {
            this.msgCode = msgCode;
            this.msg = msg;
        }
    }

}

package com.ch.web.gateway.config;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置
 *
 * @author caich
 **/
public class FastGatewayConfig implements WebMvcConfigurer {

    /**
     * 是否允许跨域
     */
    protected Boolean allowCross;

    /**
     * 编码
     */
    protected String encode;

    public String getEncode() {
        if (encode == null || encode.isEmpty()) {
            encode = "utf-8";
        }
        return encode;
    }

    public Boolean getAllowCross() {
        if (allowCross == null) {   // 默认禁止跨域
            allowCross = false;
        }
        return allowCross;
    }
}

package com.ch.web.gateway.token;

import java.util.UUID;

/**
 * 通过Uuid生成token的策略
 *
 * @author caich
 **/
public class TokenUuidGenerateStrategy<T> implements TokenGenerateStrategy<T> {

    @Override
    public String generate(T data) {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}

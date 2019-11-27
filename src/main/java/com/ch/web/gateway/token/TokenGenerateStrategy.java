package com.ch.web.gateway.token;

/**
 * 令牌签发策略
 *
 * @author caich
 **/
public interface TokenGenerateStrategy<T> {

    String generate(T data);
}

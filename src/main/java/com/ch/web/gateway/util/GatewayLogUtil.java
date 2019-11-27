package com.ch.web.gateway.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网关日志记录类
 *
 * @author caich
 **/
public class GatewayLogUtil {

    private static Logger logger = LoggerFactory.getLogger(GatewayLogUtil.class);

    public static void info(String msg) {
        if (logger.isInfoEnabled()) {
            logger.info(msg);
        }
    }

    public static void debug(String msg) {
        if (logger.isDebugEnabled()) {
            logger.debug(msg);
        }
    }

    public static void warn(String msg) {
        if (logger.isWarnEnabled()) {
            logger.warn(msg);
        }
    }

    public static void error(String msg) {
        if (logger.isErrorEnabled()) {
            logger.error(msg);
        }
    }

    public static void error(Throwable t) {
        if (logger.isErrorEnabled()) {
            logger.error(t.getMessage(), t);
        }
    }

}

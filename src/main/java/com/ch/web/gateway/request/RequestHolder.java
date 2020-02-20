package com.ch.web.gateway.request;

import com.alibaba.fastjson.JSON;
import com.ch.web.gateway.boot.FastGatewayContext;
import com.ch.web.gateway.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 网关请求
 *
 * @author caich
 **/
public class RequestHolder {

    private HttpServletRequest request;
    private volatile RequestFile requestFile;
    private Object objectCache;
    private RequestEnums.ParamType paramTypeCache;

    private RequestHolder(HttpServletRequest request) {
        this.request = request;
    }

    public static RequestHolder get(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request can not set null");
        }
        return new RequestHolder(request);
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * 上传的文件
     *
     * @return
     **/
    public RequestFile getRequestFile() {
        if (requestFile == null) {
            synchronized (this) {
                if (requestFile == null) {
                    requestFile = new RequestFile(request);
                }
            }
        }
        return requestFile;
    }

    /**
     * 获取object对象
     *
     * @param objectClass 对象类型
     * @param paramType   参数类型
     * @return
     * @see Class
     * @see RequestEnums.ParamType
     **/
    public <T> T getObjectParam(Class<T> objectClass, RequestEnums.ParamType paramType) {
        if (objectClass == null) {
            throw new IllegalArgumentException("objectClass can not be null");
        }
        if (paramType == null) {
            throw new IllegalArgumentException("paramType can not be null");
        }
        if (objectCache == null || !paramType.equals(paramTypeCache) || !objectCache.equals(objectClass)) {
            paramTypeCache = paramType;
            switch (paramType) {
                case JSON:
                    objectCache = getObjectParamByJson(objectClass);
                    break;
                case URLENCODED:
                    objectCache = getObjectParamByUrlEncoded(objectClass);
                    break;
                default:
                    objectCache = null;
            }
        }
        return objectCache == null ? null : (T) objectCache;
    }

    /**
     * 获取集合
     *
     * @param objectClass
     * @return
     * @see Class
     **/
    public <T> List<T> getArrayParamByJson(Class<T> objectClass) {
        if (objectClass == null) {
            throw new IllegalArgumentException("objectClass can not be null");
        }
        if (objectCache == null) {
            try {
                int length = 0;
                byte[] buffer = new byte[1024 * 10];
                try (InputStream inputStream = request.getInputStream();
                     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                    while ((length = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, length);
                    }
                    String json = byteArrayOutputStream.toString(FastGatewayContext.getCurrentContext().getFastGatewayConfig().getEncode());
                    objectCache = JSON.parseArray(json, objectClass);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
        return objectCache == null ? null : (List<T>) objectCache;
    }

    /**
     * 获取参数
     *
     * @param paramName
     * @param objectClass
     * @return
     **/
    public <T> T getParam(String paramName, Class<T> objectClass) {
        return (T) RequestUtil.convertByClass(request.getParameter(paramName), null, objectClass);
    }

    private <T extends Object> T getObjectParamByJson(Class<T> objectClass) {
        try {
            int length = 0;
            byte[] buffer = new byte[1024 * 10];
            try (InputStream inputStream = request.getInputStream();
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                while ((length = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, length);
                }
                String json = byteArrayOutputStream.toString(FastGatewayContext.getCurrentContext().getFastGatewayConfig().getEncode());
                return JSON.parseObject(json, objectClass);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private <T extends Object> T getObjectParamByUrlEncoded(Class<T> objectClass) {
        if (Map.class.equals(objectClass)) {
            return (T) request.getParameterMap();
        }
        try {
            T t = objectClass.newInstance();
            Field[] fields = objectClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String value = request.getParameter(field.getName());
                if (value != null) {
                    field.set(t, RequestUtil.convertByClass(value, field, field.getType()));
                }
            }
            return t;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

}

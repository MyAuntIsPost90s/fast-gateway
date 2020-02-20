package com.ch.web.gateway.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ch.web.gateway.boot.FastGatewayContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * 响应帮助类
 *
 * @author caich
 **/
public class ResponseUtil {

    /**
     * 返回json数据
     *
     * @param response
     * @param data     返回数据
     * @return
     **/
    public static void resultJson(HttpServletResponse response, Object data) throws IOException {
        String json = "";
        if (data.getClass() == String.class) {
            json = (String) data;
        } else {
            if (data != null) {
                json = JSON.toJSONString(data, SerializerFeature.DisableCircularReferenceDetect);
            }
        }
        if (response.getContentType() == null || response.getContentType().isEmpty()) {
            response.setContentType("application/json;charset=" + FastGatewayContext.getCurrentContext().getFastGatewayConfig().getEncode());
        } else {
            response.setContentType(response.getContentType().replace("text/html", "application/json"));
        }
        try (Writer writer = response.getWriter()) {
            writer.write(json);
            writer.flush();
        }
    }

}

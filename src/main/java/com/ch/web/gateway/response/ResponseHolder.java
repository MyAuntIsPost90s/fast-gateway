package com.ch.web.gateway.response;

import com.alibaba.fastjson.JSONObject;
import com.ch.web.gateway.util.GatewayLogUtil;
import com.ch.web.gateway.util.ResponseUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * 网关响应
 *
 * @author caich
 **/
public class ResponseHolder {

    private HttpServletResponse response;

    private final static Integer SUCCESS_CODE = 1; // 成功码

    private final static Integer FAIL_CODE = 0;    // 失败码

    private final static String SUCCESS_MSG = "操作成功";    // 成功提示

    private final static String FAIL_MSG = "操作失败";    // 成功提示

    private ResponseHolder(HttpServletResponse response) {
        this.response = response;
    }

    public static ResponseHolder get(HttpServletResponse response) {
        return new ResponseHolder(response);
    }

    /**
     * 成功
     *
     * @param data
     * @return
     **/
    public void success(Object data) {
        success(SUCCESS_MSG, data, null);
    }

    /**
     * 成功
     *
     * @param msg
     * @return
     **/
    public void success(String msg) {
        success(msg, null, null);
    }

    /**
     * 成功
     *
     * @param msg
     * @param data
     * @return
     **/
    public void success(String msg, Object data) {
        success(msg, data, null);
    }

    /**
     * 成功
     *
     * @param msg
     * @param msgCode
     * @return
     **/
    public void success(String msg, String msgCode) {
        success(msg, null, msgCode);
    }

    /**
     * 成功
     *
     * @param data
     * @param msgCode
     * @return
     **/
    public void success(Object data, String msgCode) {
        success(FAIL_MSG, data, msgCode);
    }

    /**
     * 成功
     *
     * @param msg
     * @param data
     * @param msgCode
     * @return
     **/
    public void success(String msg, Object data, String msgCode) {
        JSONObject result = new JSONObject();
        result.put("data", data);
        result.put("msg", msg);
        result.put("code", SUCCESS_CODE);
        result.put("msgCode", msgCode);
        entity(result);
    }

    /**
     * 失败
     *
     * @param data
     * @return
     **/
    public void fail(Object data) {
        fail(FAIL_MSG, data, null);
    }

    /**
     * 失败
     *
     * @param msg
     * @return
     **/
    public void fail(String msg) {
        fail(msg, null, null);
    }

    /**
     * 失败
     *
     * @param msg
     * @param msgCode
     * @return
     **/
    public void fail(String msg, String msgCode) {
        fail(msg, null, msgCode);
    }

    /**
     * 失败
     *
     * @param data
     * @param msgCode
     * @return
     **/
    public void fail(Object data, String msgCode) {
        fail(FAIL_MSG, data, msgCode);
    }

    /**
     * 失败
     *
     * @param msg
     * @param data
     * @return
     **/
    public void fail(String msg, Object data) {
        fail(msg, data, null);
    }

    /**
     * 失败
     *
     * @param msg
     * @param data
     * @param msgCode
     * @return
     **/
    public void fail(String msg, Object data, String msgCode) {
        JSONObject result = new JSONObject();
        result.put("data", data);
        result.put("msg", msg);
        result.put("code", FAIL_CODE);
        result.put("msgCode", msgCode);
        entity(result);
    }

    /**
     * 返回自定义数据
     *
     * @param data 自定义参数
     * @return
     **/
    public void entity(Object data) {
        try {
            ResponseUtil.resultJson(response, data);
        } catch (Exception e) {
            GatewayLogUtil.error(e);
        }
    }
}

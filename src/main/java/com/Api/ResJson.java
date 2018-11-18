package com.Api;

import org.json.simple.JSONObject;

public class ResJson {
    /*
        统一json返回格式
        @param status: 状态码
        @param message: 状态吗相关信息
        @param data: 数据
     */
    public static JSONObject generateResJson(int status, String message, Object data) {
        JSONObject json = new JSONObject();
        json.put("status", new Integer(status));
        json.put("message", message);
        json.put("data", data);
        return json;
    }
}

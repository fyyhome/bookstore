package com.Api;

import org.json.JSONObject;

import javax.servlet.ServletInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResJson {
    /*
        统一json返回格式
        @param status: 状态码(1:成功,2:无效token,3:无权限)
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

    public static String getRequestBody(ServletInputStream sp) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sp, "utf-8"));
        StringBuffer stringBuffer = new StringBuffer();
        String iString = bufferedReader.readLine();
        while (iString!= null) {
            stringBuffer.append(iString);
            iString = bufferedReader.readLine();
        }
        return stringBuffer.toString();
    }
}

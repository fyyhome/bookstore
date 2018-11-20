package com.Api;

import com.MySql.MysqlUtil;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(name = "Register")
public class Register extends HttpServlet {
    JSONObject resJson;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] params = new String[2];
        parse(ResJson.getRequestBody(request.getInputStream()), params);
        String sql = "insert into user(user_name, user_password) values(?,?)";
        Connection conn = MysqlUtil.getConnection();
        if (MysqlUtil.excutUpdate(conn, sql, params) > 0) {
            resJson = ResJson.generateResJson(1, "请求成功", "注册成功");
        } else {
            resJson = ResJson.generateResJson(2, "请求失败", "注册失败");
        }
        response.getWriter().println(resJson);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void parse(String data, String[] params) {
        JSONObject json = new JSONObject(data);
        params[0] = json.getString("user_num");
        params[1] = json.getString("user_pwd");
    }
}

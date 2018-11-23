package com.Api;

import com.MySql.MysqlUtil;
import com.WebToken.JJwtUtil;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(name = "Login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] params = new String[2];
        parse(ResJson.getRequestBody(request.getInputStream()), params);
        String sql = "select * from user where user_name = '" + params[0] + "'";
        Connection conn = MysqlUtil.getConnection();
        ResultSet rs = MysqlUtil.excutQuery(conn, sql, null);
        try {
            String password = "";
            String user_id = "";
            while (rs.next()) {
                password = rs.getString("user_password");
                user_id = rs.getString("user_id");
            }
            if (password.equals("")) {
                response.getWriter().println(ResJson.generateResJson(2, "登录失败", "用户名不存在"));
            } else if (params[1].equals(password)) {
                String token = JJwtUtil.createJWT(user_id, params[0]);
                JSONObject data = new JSONObject();
                data.put("token", token);
                response.getWriter().println(ResJson.generateResJson(1, "请求成功", data));
            } else {
                response.getWriter().println(ResJson.generateResJson(3, "登录失败", "密码错误"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MysqlUtil.closeAll(conn,null,rs);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void parse(String data, String[] params) {
        JSONObject json = new JSONObject(data);
        params[0] = json.getString("user_num");
        params[1] = json.getString("user_pwd");
    }
}

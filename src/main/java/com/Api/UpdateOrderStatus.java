package com.Api;

import com.MySql.MysqlUtil;
import com.WebToken.VerifyToken;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(name = "UpdateOrderStatus")
public class UpdateOrderStatus extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            response.getWriter().println(ResJson.generateResJson(2, "未登录", "未登录"));
        } else {
            Connection conn = MysqlUtil.getConnection();
            if (VerifyToken.myVerify(token)) {
                String mysql = "select * from user where user_id = " + VerifyToken.parseUserId(token);
                ResultSet rs = MysqlUtil.excutQuery(conn, mysql, null);
                int user_type = -1;
                try {
                    while (rs.next()) {
                        user_type = rs.getInt( "user_type");
                    }
                    if (user_type == 1) {
                        String[] param = new String[1];
                        parse(ResJson.getRequestBody(request.getInputStream()), param);
                        String sql = "update orders set is_finsh = " + 1 + "where order_id = " + param[0];
                        if(MysqlUtil.excutUpdate(conn, sql, null) > 0) {
                            response.getWriter().println(ResJson.generateResJson(1, "修改成功", "无"));
                        } else {
                            response.getWriter().println(ResJson.generateResJson(5, "修改出了点意外", "无"));
                        }
                    } else {
                        response.getWriter().println(ResJson.generateResJson(3, "没有管理员权限", "无"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                response.getWriter().println(ResJson.generateResJson(4, "无效token", "无"));
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // doPost(request,response);
    }

    private void parse(String data, String[] param) {
        JSONObject json = new JSONObject(data);
        param[0] = json.getString("order_id");
    }
}

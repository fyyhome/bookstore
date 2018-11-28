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
import java.sql.SQLException;

// 删除购物车的书籍
@WebServlet(name = "DeleteCarBook")
public class DeleteCarBook extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            response.getWriter().println(ResJson.generateResJson(4, "未登录", "无"));
        } else {
            if (VerifyToken.myVerify(token)) {
                String user_id = VerifyToken.parseUserId(token);
                JSONObject reqBody = new JSONObject(ResJson.getRequestBody(request.getInputStream()));
                String book_id = reqBody.getString("book_id");
                try {
                    Connection conn = MysqlUtil.getConnection();
                    String sql = "delete from shop_car where user_id = " + user_id + " and book_id = " + book_id;
                    if (MysqlUtil.excutUpdate(conn, sql, null) > 0) {
                        response.getWriter().println(ResJson.generateResJson(1, "删除成功", "无"));
                    } else {
                        response.getWriter().println(ResJson.generateResJson(3, "操作失败", "原因请查数据库"));
                    }
                } catch (Exception e) {
                    response.getWriter().println(ResJson.generateResJson(2, "数据库操作异常", e.toString()));
                    e.printStackTrace();
                }
            } else {
                response.getWriter().println(ResJson.generateResJson(3, "token无效", "无"));
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

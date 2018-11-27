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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


// 购物车里的书籍增一或减一操作 type: -1(reduce)/1(add)
@WebServlet(name = "UpdateCarBookCount")
public class UpdateCarBookCount extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token.equals("") || token == null) {
            response.getWriter().println(ResJson.generateResJson(4, "未登录", "无"));
        } else {
            if (VerifyToken.myVerify(token)) {
                String user_id = VerifyToken.parseUserId(token);
                JSONObject reqBody = new JSONObject(ResJson.getRequestBody(request.getInputStream()));
                int type = reqBody.getInt("type");
                int book_id = reqBody.getInt("book_id");
                int book_count = -1;
                String querySql = "select book_count from shop_car where user_id = " + user_id + " and book_id = " + book_id;
                try {
                    Connection conn = MysqlUtil.getConnection();
                    ResultSet rs = MysqlUtil.excutQuery(conn, querySql, null);
                    while (rs.next()) {
                        book_count = rs.getInt("book_count");
                    }
                    if (book_count > 0) {
                        String sql = "update shop_car set book_count = ";
                        if (type == -1) {
                            sql += (book_count - 1) + " where user_id = " + user_id + " and book_id = " + book_id;
                        } else if (type == 1) {
                            sql += (book_count + 1) + " where user_id = " + user_id + " and book_id = " + book_id;
                        } else {
                            response.getWriter().println(ResJson.generateResJson(6, "传入参数错误", "无"));
                        }
                        if (MysqlUtil.excutUpdate(conn, sql, null) > 0) {
                            response.getWriter().println(ResJson.generateResJson(1, "更新成功", "无"));
                        } else {
                            response.getWriter().println(ResJson.generateResJson(5, "操作失败", "原因请查数据库"));
                        }

                    } else {
                        response.getWriter().println(ResJson.generateResJson(3, "操作失败，数据库不存在此条信息", "无"));
                    }
                } catch (Exception e) {
                    response.getWriter().println(ResJson.generateResJson(2, "操作失败", e.toString()));
                    e.printStackTrace();
                }
            } else {
                response.getWriter().println(ResJson.generateResJson(4, "无效token", "无"));
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

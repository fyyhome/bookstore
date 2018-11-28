package com.Api;

import com.MySql.MysqlUtil;
import com.WebToken.JJwtUtil;
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
import java.sql.SQLException;

// 登录用户添加购物车
@WebServlet(name = "AddBookWithToken")
public class AddBookWithToken extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            response.getWriter().println(ResJson.generateResJson(4, "未登录", "无"));
        } else {
            if (VerifyToken.myVerify(token)) {
                String user_id = VerifyToken.parseUserId(token);
                JSONObject json = new JSONObject(ResJson.getRequestBody(request.getInputStream()));
                String sql = "select * from shop_car where user_id = " + user_id + " and book_id = " + json.getInt("book_id");
                try {
                    Connection conn = MysqlUtil.getConnection();
                    ResultSet rs = MysqlUtil.excutQuery(conn, sql, null);
                    if (rs.next()) {
                        int count = rs.getInt("book_count");
                        count += json.getInt("book_count");
                        String updateSql = "update shop_car set book_count = " + count + " where user_id = " + user_id + " and book_id = " + json.getInt("book_id");
                        if(MysqlUtil.excutUpdate(conn, updateSql, null) > 0) {
                            response.getWriter().println(ResJson.generateResJson(1, "添加成功", "无"));
                        } else {
                            response.getWriter().println(ResJson.generateResJson(2, "添加失败", "未知意外"));
                        }
                    } else {
                        String updateSql = "inser into shop_car(user_id,book_id,book_count) values(?,?,?)";
                        String[] updateParam = new String[3];
                        updateParam[0] = user_id;
                        updateParam[1] = json.getString("book_id");
                        updateParam[2] = json.getString("book_count");
                        if(MysqlUtil.excutUpdate(conn, updateSql, updateParam) > 0) {
                            response.getWriter().println(ResJson.generateResJson(1, "添加成功", "无"));
                        } else {
                            response.getWriter().println(ResJson.generateResJson(2, "添加失败", "未知意外"));
                        }
                    }
                } catch (Exception e) {
                    response.getWriter().println(ResJson.generateResJson(2,"数据库操作错误", e.toString()));
                    e.printStackTrace();
                }
            } else {
                response.getWriter().println(ResJson.generateResJson(3, "无效token", "无"));
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

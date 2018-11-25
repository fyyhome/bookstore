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

@WebServlet(name = "AddBookToSql")
public class AddBookToSql extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            response.getWriter().println(ResJson.generateResJson(4, "未登录", "未登录"));
        } else {

            if (VerifyToken.myVerify(token)) {
                String mysql = "select * from user where user_id = " + VerifyToken.parseUserId(token);
                int user_type = -1;
                try {
                    Connection conn = MysqlUtil.getConnection();
                    ResultSet rs = MysqlUtil.excutQuery(conn, mysql, null);
                    while (rs.next()) {
                        user_type = rs.getInt( "user_type");
                    }
                    if (user_type == 1) {
                        String[] param = new String[7];
                        int c_id = parse(ResJson.getRequestBody(request.getInputStream()), param);
                        String sql = "insert into books(book_name,book_author,book_price,book_publishing,c_id,book_smimg,book_mdimg,book_describe)" +
                                " values(?,?,?,?," + c_id + ",?,?,?)";
                        if (MysqlUtil.excutUpdate(conn, sql, param) > 0) {
                            response.getWriter().println(ResJson.generateResJson(1, "添加成功", "无"));
                        } else {
                            response.getWriter().println(ResJson.generateResJson(2, "添加出了点意外", "无"));
                        }
                    } else {
                        response.getWriter().println(ResJson.generateResJson(5, "没有管理员权限", "无"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                response.getWriter().println(ResJson.generateResJson(3, "无效token", "无"));
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private int parse(String data, String[] params) {
        JSONObject json = new JSONObject(data);
        params[0] = json.getString("book_name");
        params[1] = json.getString("book_author");
        params[2] = json.getString("book_price");
        params[3] = json.getString("book_publishing");
        params[4] = json.getString("book_smimg");
        params[5] = json.getString("book_mdimg");
        params[6] = json.getString("book_describe");
        return json.getInt("c_id");
    }
}

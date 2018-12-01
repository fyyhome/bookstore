package com.Api;

import com.MySql.MysqlUtil;
import com.WebToken.VerifyToken;
import org.json.JSONArray;
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


// 登录用户提交订单
@WebServlet(name = "SubmitOrder")
public class SubmitOrder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            response.getWriter().println(ResJson.generateResJson(4, "未登录", "无"));
        } else {
            if (VerifyToken.myVerify(token)) {
                String user_id = VerifyToken.parseUserId(token);
                JSONArray jsonArr = new JSONArray(ResJson.getRequestBody(request.getInputStream()));
                int[] params = new int[4];
                params[2] = Integer.parseInt(user_id);
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject json = jsonArr.getJSONObject(i);
                    params[0] = getOrderId();
                    params[1] = json.getInt("book_id");
                    params[3] = json.getInt("book_count");
                    String sql = "insert into orders(order_id,book_id,user_id,book_count) values(?,?,?,?)";
                    try {
                        Connection conn = MysqlUtil.getConnection();
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        for (int j = 0; j < params.length; i++) {
                            pstmt.setInt(i+1, params[i]);
                        }
                        if (pstmt.executeUpdate() > 0) {
                            response.getWriter().println(ResJson.generateResJson(1, "提交成功", "无"));
                        } else {
                            response.getWriter().println(ResJson.generateResJson(2, "提交出了点意外", "无"));
                        }
                    } catch (Exception e) {
                        response.getWriter().println(ResJson.generateResJson(2, "提交出了点意外", e.toString()));
                        e.printStackTrace();
                    }
                }
            } else {
                response.getWriter().println(ResJson.generateResJson(3, "无效token", "无"));
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private int getOrderId() {
        int order_id = 0;
        String sql = "select MAX(order_id) from orders";
        try {
            Connection conn = MysqlUtil.getConnection();
            ResultSet rs = MysqlUtil.excutQuery(conn, sql, null);
            while (rs.next()) {
                order_id = rs.getInt("order_id");
            }
            order_id++;
            rs.close();
            conn.prepareStatement(sql).close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order_id;
    }
}

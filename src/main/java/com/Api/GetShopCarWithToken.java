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


// 登录用户查看购物车
@WebServlet(name = "GetShopCarWithToken")
public class GetShopCarWithToken extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")){
            response.getWriter().println(ResJson.generateResJson(4, "未登录", "无"));
        } else {
            if (VerifyToken.myVerify(token)) {
                String user_id = VerifyToken.parseUserId(token);
                String querySql = "select * from shop_car where user_id = " + user_id;
                ArrayList<JSONObject> dataArr = new ArrayList<>();
                Connection conn = MysqlUtil.getConnection();
                ResultSet rs = MysqlUtil.excutQuery(conn, querySql, null);
                try {
                    while (rs.next()) {
                        JSONObject data = new JSONObject();
                        data.put("book_count", rs.getInt("book_count"));
                        String mysql = "select * from books where book_id = " + rs.getInt("book_id");
                        ResultSet myrs = MysqlUtil.excutQuery(conn, mysql, null);
                        while (myrs.next()) {
                            data.put("book_id", myrs.getInt("book_id"));
                            data.put("book_name", myrs.getString("book_name"));
                            data.put("book_author", myrs.getString("book_author"));
                            data.put("book_price", myrs.getString("book_price"));
                            data.put("book_publishing", myrs.getString("book_publishing"));
                            data.put("book_smimg", myrs.getString("book_smimg"));
                        }
                        dataArr.add(data);
                    }
                    response.getWriter().println(ResJson.generateResJson(1, "获取成功", dataArr));
                } catch (Exception e) {
                    e.printStackTrace();
                    response.getWriter().println(ResJson.generateResJson(2, "获取失败", "出了点意外"));
                }
                MysqlUtil.closeAll(conn,null,rs);
            } else {
                response.getWriter().println(ResJson.generateResJson(3, "无效token", "无"));
            }
        }
    }
}

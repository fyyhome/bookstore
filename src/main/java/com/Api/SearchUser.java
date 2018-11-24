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

// 后台用户信息查询
@WebServlet(name = "SearchUser")
public class SearchUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    // 该请求要后台管理员权限
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            response.getWriter().println(ResJson.generateResJson(4, "未登录", "未登录"));
        } else {
            Connection conn = MysqlUtil.getConnection();
            if (VerifyToken.myVerify(token)) {
               String mysql = "select * from user where user_id = " + VerifyToken.parseUserId(token);
               ResultSet rs = MysqlUtil.excutQuery(conn, mysql, null);
               int user_type = -1;
               try {
                   while (rs.next()) {
                       user_type = rs.getInt("user_type");
                   }
                   if (user_type == 1) {
                       String user_name = request.getParameter("user_name");
                       ArrayList<JSONObject> userArr = new ArrayList<>();
                       String sql = null;
                       if (user_name == null) {
                           sql = "select * from user";
                       } else {
                           sql = "select * from user where user_name = '" + user_name + "'";
                       }
                       rs = MysqlUtil.excutQuery(conn, sql, null);
                       while (rs.next()) {
                           JSONObject user = new JSONObject();
                           user.put("user_id", rs.getString("user_id"));
                           user.put("user_name", rs.getString("user_name"));
                           user.put("user_type", rs.getString("user_type"));
                           userArr.add(user);
                       }
                       if (userArr.size() == 0) {
                           response.getWriter().println(ResJson.generateResJson(2, "该用户不存在", "无"));
                       } else {
                           response.getWriter().println(ResJson.generateResJson(1, "请求成功", userArr));
                       }
                   } else {
                       response.getWriter().println(ResJson.generateResJson(5, "没有管理员权限", "无"));
                   }
                   MysqlUtil.closeAll(conn, null, rs);
               } catch (Exception e) {
                   e.printStackTrace();
               }
            } else {
                response.getWriter().println(ResJson.generateResJson(3, "无效token", "无"));
            }
        }
    }
}

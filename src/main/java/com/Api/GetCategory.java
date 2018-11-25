package com.Api;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.MySql.MysqlUtil;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


// 获取书籍类别
public class GetCategory extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String sql = "select * from category";
        try {
            Connection conn = MysqlUtil.getConnection();
            ResultSet rs = MysqlUtil.excutQuery(conn, sql, null);
            ArrayList<JSONObject> categoryArr = new ArrayList<>();
            while (rs.next()) {
                int c_id = rs.getInt("c_id");
                String c_name = rs.getString("c_name");
                JSONObject category = new JSONObject();
                category.put("id", c_id);
                category.put("name", c_name);
                categoryArr.add(category);
            }
            MysqlUtil.closeAll(conn, conn.prepareStatement(sql), rs);
            PrintWriter out = response.getWriter();
            out.println(ResJson.generateResJson(1, "请求成功", categoryArr));
        } catch (Exception e) {
            try {
                response.getWriter().println(ResJson.generateResJson(2, "数据库操作失败", "无"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}

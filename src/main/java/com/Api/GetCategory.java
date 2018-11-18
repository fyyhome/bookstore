package com.Api;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.MySql.MysqlUtil;
import org.json.simple.JSONObject;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GetCategory extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        String sql = "select * from category";
        Connection conn = MysqlUtil.getConnection();
        ResultSet rs = MysqlUtil.excutQuery(conn, sql, null);
        try {
            ArrayList<JSONObject> categoryArr = new ArrayList<>();
            while (rs.next()) {
                int c_id = rs.getInt("c_id");
                String c_name = rs.getString("c_name");
                JSONObject category = new JSONObject();
                category.put("id", c_id);
                category.put("name", c_name);
                categoryArr.add(category);
            }
            PrintWriter out = response.getWriter();
            out.println(ResJson.generateResJson(1, "请求成功", categoryArr));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

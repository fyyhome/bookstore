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
    protected JSONObject resData = new JSONObject();

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
                System.out.println(c_name);
                JSONObject category = new JSONObject();
                category.put("id", c_id);
                category.put("name", c_name);
                categoryArr.add(category);
            }
            resData.put("status", new Integer(1));
            resData.put("message", "获取成功");
            resData.put("data", categoryArr);
            PrintWriter out = response.getWriter();
            out.println(resData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

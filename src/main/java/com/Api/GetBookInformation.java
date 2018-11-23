package com.Api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.MySql.MysqlUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class GetBookInformation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out=response.getWriter();
        Connection conn=MysqlUtil.getConnection();
        String sbook_id=request.getParameter("book_id");
        int book_id=Integer.parseInt(sbook_id);
       // int book_id=100000158;
        try{
            String sql="select * from books where book_id=?";
            PreparedStatement psta=conn.prepareStatement(sql);
            psta.setInt(1,book_id);
            ResultSet rs=psta.executeQuery();
            JSONObject book = new JSONObject();
            while(rs.next())
            {
                book.put("id",book_id);
                String book_name=rs.getString("book_name");
                book.put("name",book_name);
                String book_author=rs.getString("book_author");
                book.put("author",book_author);
                float book_price=rs.getFloat("book_price");
                book.put("price",book_price);
                String book_publishing=rs.getString("book_publishing");
                book.put("publishing",book_publishing);
                String book_smimg=rs.getString("book_smimg");
                book.put("smimg",book_smimg);
                String book_mdimg=rs.getString("book_mdimg");
                book.put("mdimg",book_mdimg);
                String book_describe=rs.getString("book_describe");
                book.put("describe",book_describe);
            }
            out.println(ResJson.generateResJson(1,"书籍信息请求成功",book));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

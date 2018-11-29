package com.Api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.MySql.MysqlUtil;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
public class GetSpecialKind extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Connection conn = MysqlUtil.getConnection();
        String sc_id=request.getParameter("c_id");//传入书籍编号;
        if(sc_id==null)
        {
            response.getWriter().println("获取信息失败,请正确传入书籍号");
            return ;
        }
        int c_id=Integer.parseInt(sc_id);
        try{
            PrintWriter out=response.getWriter();
            ArrayList<JSONObject> bookArr = new ArrayList<>();
            String sql="select * from books where c_id=?";
            PreparedStatement psta=conn.prepareStatement(sql);
            psta.setInt(1,c_id);
            ResultSet rs=psta.executeQuery();
            while(rs.next())
            {
                JSONObject book = new JSONObject();
                int book_id=rs.getInt("book_id");
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
               // out.println(book_id+"   "+book_name+"     "+book_author+"     "+book_price+"      "+book_publishing+"     "+ book_smimg+"     "+book_mdimg+"    "+book_describe);
                bookArr.add(book);
            }
            if(bookArr.size()==0)
                out.println(ResJson.generateResJson(-1,"书籍信息请求失败","书籍类别号非法"));
            else
            out.println(ResJson.generateResJson(1, "书籍信息请求成功", bookArr));
            MysqlUtil.closeAll(conn,psta,rs);
        }
        catch (Exception e)
        {
            System.out.println("Get Special Kind Error");
        }

    }
}

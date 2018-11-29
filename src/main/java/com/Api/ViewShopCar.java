package com.Api;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import com.MySql.MysqlUtil;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
public class ViewShopCar extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out=response.getWriter();
        HttpSession session=request.getSession();
        ArrayList<JSONObject> bookArr=(ArrayList<JSONObject>)session.getAttribute("bookArr");
        ArrayList<JSONObject> bookInformation=new ArrayList<JSONObject>();
        if(bookArr==null)
        {
            out.println(ResJson.generateResJson(-1,"Sorry,当前购物车为空~","Sorry,当前购物车为空~"));
        }
        else{
            Connection conn = MysqlUtil.getConnection();
            for(int i=0;i<bookArr.size();i++)
            {
                JSONObject data=bookArr.get(i);
                int book_id=data.getInt("book_id");
                int book_num=data.getInt("book_num");
                try {
                    String sql = "select * from books where book_id=?";
                    PreparedStatement psta = conn.prepareStatement(sql);
                    psta.setInt(1, book_id);
                    ResultSet rs = psta.executeQuery();
                    while (rs.next()) {
                        JSONObject book = new JSONObject();
                        rs.getInt("book_id");
                        book.put("id", book_id);
                        String book_name = rs.getString("book_name");
                        book.put("name", book_name);
                        String book_author = rs.getString("book_author");
                        book.put("author", book_author);
                        float book_price = rs.getFloat("book_price");
                        book_price = book_price * book_num;
                        book.put("price", book_price);
                        String book_publishing = rs.getString("book_publishing");
                        book.put("publishing", book_publishing);
                        String book_smimg = rs.getString("book_smimg");
                        book.put("smimg", book_smimg);
                        String book_mdimg = rs.getString("book_mdimg");
                        book.put("mdimg", book_mdimg);
                        String book_describe = rs.getString("book_describe");
                        book.put("describe", book_describe);
                        book.put("book_num", book_num);
                        bookInformation.add(book);
                    }
                    psta.close();
                    rs.close();
                }
                catch(Exception e)
                {
                    System.out.println("查找书籍信息错误");
                }

            }
            out.println(ResJson.generateResJson(1, "购物车信息请求成功", bookInformation));
            try{
                conn.close();
            }
            catch (Exception e)
            {
                System.out.println("数据库异常，无法关闭");
            }
        }

    }
}

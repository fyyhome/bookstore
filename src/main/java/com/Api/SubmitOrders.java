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
import java.sql.Statement;
import java.util.ArrayList;

public class SubmitOrders extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out=response.getWriter();
        String suserId=request.getParameter("userId");
        int userId=Integer.parseInt(suserId);
        String[] book_idArr=request.getParameterValues("book_idArr");//从前端传来的是名叫book_idArr的字符串数组
       // String[] book_idArr={"100000158","100000611"};
        Connection conn=MysqlUtil.getConnection();
        ArrayList<JSONObject> bookArr=new ArrayList<JSONObject>();
        try{
            String sql3="select * from shop_car where user_id=?";
            PreparedStatement psta3=conn.prepareStatement(sql3);
            psta3.setInt(1,userId);
            ResultSet rs3=psta3.executeQuery();
            while(rs3.next())
            {
                JSONObject data=new JSONObject();
                int book_id=rs3.getInt("book_id");
                data.put("book_id",book_id);
                int book_num=rs3.getInt("book_count");
                data.put("book_num",book_num);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        int order_id=0;
        try{
            String sql="select MAX(order_id) from orders";
            Statement sta=conn.createStatement();
            ResultSet rs=sta.executeQuery(sql);
            while(rs.next())
            {
                order_id=rs.getInt(1);
            }
            sta.close();
            rs.close();
            order_id++;
           // out.println(order_id);
        }
        catch (Exception e)
        {
            System.out.println("获取订单号失败");
        }

        float price=0;
        for(int i=0;i<book_idArr.length;i++) {
            String spreInsertId = book_idArr[i];
            int preInsertId = Integer.parseInt(spreInsertId);
            for (int j = 0; j < bookArr.size(); j++) {
                JSONObject data = bookArr.get(j);
                int id = data.getInt("book_id");
                if (id == preInsertId) {
                    try{
                        int book_num = data.getInt("book_num");
                        String sql2="select * from books where book_id=?";
                        PreparedStatement psta2=conn.prepareStatement(sql2);
                        psta2.setInt(1,id);
                        ResultSet rs2=psta2.executeQuery();
                        float book_price=0;
                        while(rs2.next())
                        {
                             book_price=rs2.getFloat("book_price");
                        }
                        price+=book_price*book_num;
                        String sql1 = "insert into orders values (?,?,?,?,?)";
                        PreparedStatement psta = conn.prepareStatement(sql1);
                        psta.setInt(1, order_id);
                        psta.setInt(2, id);
                        psta.setInt(3, userId);
                        psta.setInt(4, book_num);
                        psta.setInt(5, 0);
                        psta.executeUpdate();
                       String sql4="delete shop_car where book_id=?";
                       PreparedStatement psta4=conn.prepareStatement(sql4);
                       psta4.setInt(1,id);
                       psta4.executeUpdate();
                        psta.close();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

            try{
                conn.close();
            }
            catch (Exception e)
            {
                System.out.println("数据库关闭失败");
            }
            JSONObject book_price=new JSONObject();
            book_price.put("book_price",price);
            out.println(ResJson.generateResJson(1,"compute price successfully",book_price));
    }
}
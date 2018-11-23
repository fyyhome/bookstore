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
        PrintWriter out=response.getWriter();
        String[] book_idArr=request.getParameterValues("book_idArr");//从前端传来的是名叫book_idArr的字符串数组
       // String[] book_idArr={"100000158","100000611"};
        HttpSession session=request.getSession();
        ArrayList<JSONObject> bookArr=(ArrayList<JSONObject>)session.getAttribute("bookArr");
        Connection conn=MysqlUtil.getConnection();
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
            out.println(order_id);
        }
        catch (Exception e)
        {
            System.out.println("获取订单号失败");
        }
        int userId=3;
        for(int i=0;i<book_idArr.length;i++) {
            String spreInsertId = book_idArr[i];
            int preInsertId = Integer.parseInt(spreInsertId);
            out.println(preInsertId);
            for (int j = 0; j < bookArr.size(); j++) {
                JSONObject data = bookArr.get(j);
                int id = data.getInt("book_id");
                if (id == preInsertId) {
                    int book_num = data.getInt("book_num");
                    try {
                        String sql1 = "insert into orders values (?,?,?,?,?)";
                        PreparedStatement psta = conn.prepareStatement(sql1);
                        psta.setInt(1, order_id);
                        psta.setInt(2, id);
                        psta.setInt(3, userId);
                        psta.setInt(4, book_num);
                        psta.setInt(5, 0);
                        psta.executeUpdate();
                        bookArr.remove(j);
                        psta.close();
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
                session.setAttribute("bookArr",bookArr);
            try{
                conn.close();
            }
            catch (Exception e)
            {
                System.out.println("数据库关闭失败");
            }
    }
}
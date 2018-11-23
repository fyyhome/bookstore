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

public class ViewAllOrders extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out=response.getWriter();
        Connection conn=MysqlUtil.getConnection();
        ArrayList<Integer> order_idArr_status0=new ArrayList<Integer>();
        ArrayList<Integer> order_idArr_status1=new ArrayList<Integer>();
        ArrayList<ArrayList<JSONObject>> orderArr_status0=new ArrayList<ArrayList<JSONObject>>();
        ArrayList<ArrayList<JSONObject>> orderArr_status1=new ArrayList<ArrayList<JSONObject>>();
        try{
            String sql1="select order_id from orders where is_finsh=0 group by order_id";
            Statement sta1=conn.createStatement();
            ResultSet rs1=sta1.executeQuery(sql1);
            while(rs1.next())
            {
                int order_id=rs1.getInt(1);
                order_idArr_status0.add(order_id);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try{
            String sql2="select order_id from orders where is_finsh=1 group by order_id";
            Statement sta2=conn.createStatement();
            ResultSet rs2=sta2.executeQuery(sql2);
            while(rs2.next())
            {
                int order_id=rs2.getInt(1);
                order_idArr_status1.add(order_id);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        for(int i=0;i<order_idArr_status0.size();i++)
        {
            try{
                int order_id=order_idArr_status0.get(i);
                ArrayList<JSONObject> order=new ArrayList<JSONObject>();
                String sql3="select * from orders where order_id=?";
                PreparedStatement psta3=conn.prepareStatement(sql3);
                psta3.setInt(1,order_id);
                ResultSet rs3=psta3.executeQuery();
                while(rs3.next())
                {
                    JSONObject order_information=new JSONObject();
                    order_information.put("order_id",order_id);
                    int user_id=rs3.getInt("user_id");
                    order_information.put("user_id",user_id);
                    int book_id=rs3.getInt("book_id");
                    order_information.put("book_id",book_id);
                    int book_num=rs3.getInt("book_count");
                    order_information.put("book_num",book_num);
                    order_information.put("is_finsh",0);
                    order.add(order_information);
                }
                orderArr_status0.add(order);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        for(int i=0;i<order_idArr_status1.size();i++)
        {
            try{
                int order_id=order_idArr_status1.get(i);
                String sql4="select * from orders where order_id=?";
                PreparedStatement psta4=conn.prepareStatement(sql4);
                psta4.setInt(1,order_id);
                ResultSet rs4=psta4.executeQuery(sql4);
                ArrayList<JSONObject> order=new ArrayList<JSONObject>();
                while(rs4.next())
                {
                    JSONObject order_information=new JSONObject();
                    order_information.put("order_id",order_id);
                    int user_id=rs4.getInt("user_id");
                    order_information.put("user_id",user_id);
                    int book_id=rs4.getInt("book_id");
                    order_information.put("book_id",book_id);
                    int book_num=rs4.getInt("book_count");
                    order_information.put("book_num",book_num);
                    order_information.put("is_finsh",1);
                    order.add(order_information);
                }
                orderArr_status1.add(order);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        out.println(ResJson.generateResJson(1,"未完成订单",orderArr_status0));
        out.println(ResJson.generateResJson(1,"已完成订单",orderArr_status1));
    }
}
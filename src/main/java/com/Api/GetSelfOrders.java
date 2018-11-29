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


public class GetSelfOrders extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String suser_id=request.getParameter("user_id");
        int user_id=Integer.parseInt(suser_id);
        PrintWriter out=response.getWriter();
        ArrayList<Integer> orderIdArr=new ArrayList<Integer>();
        ArrayList<ArrayList<JSONObject>> selfOrders=new ArrayList<ArrayList<JSONObject>>();
        Connection conn=MysqlUtil.getConnection();
        int orderNum=0;
        try{
            String sql1="select distinct order_id from orders where user_id=?";
            PreparedStatement psta1=conn.prepareStatement(sql1);
            psta1.setInt(1,user_id);
            ResultSet rs1=psta1.executeQuery();
            while(rs1.next())
            {
                int order_id=rs1.getInt(1);
                out.println(order_id);
                orderIdArr.add(order_id);
            }
            psta1.close();
            rs1.close();
        }
        catch (Exception e)
        {
            System.out.println("获取订单号失败");
        }
        for(int i=0;i<orderIdArr.size();i++)
        {
            int order_id=orderIdArr.get(i);
            try{
                String sql2="select * from orders where order_id=?";
                PreparedStatement psta2=conn.prepareStatement(sql2);
                psta2.setInt(1,order_id);
                ResultSet rs2=psta2.executeQuery();
                ArrayList<JSONObject> order=new ArrayList<JSONObject>();
                while(rs2.next())
                {
                    JSONObject order_information=new JSONObject();
                    order_information.put("order_id",order_id);
                    int book_id=rs2.getInt("book_id");
                    order_information.put("book_id",book_id);
                    int book_num=rs2.getInt("book_count");
                    order_information.put("book_num",book_num);
                    order_information.put("user_id",user_id);
                    int is_finsh=rs2.getInt("is_finsh");
                    order_information.put("is_finsh",is_finsh);
                    order.add(order_information);
                }
                selfOrders.add(order);
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
        }
        response.getWriter().println(ResJson.generateResJson(1,"订单信息请求成功",selfOrders));
    }
}

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

public class UpdateOrderStatus extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String suser_id=request.getParameter("user_id");
        String sorder_id=request.getParameter("order_id");
        String sstatus=request.getParameter("status");
        int user_id=Integer.parseInt(suser_id);
        int order_id=Integer.parseInt(sorder_id);
        int status=Integer.parseInt(sstatus);
        Connection conn=MysqlUtil.getConnection();
        try{
            String sql="update orders set status=? where user_id=? and order_id=?";
            PreparedStatement psta=conn.prepareStatement(sql);
            psta.setInt(1,status);
            psta.setInt(2,user_id);
            psta.setInt(3,order_id);
            psta.executeUpdate();
            conn.close();
            psta.close();
        }
        catch (Exception e)
        {
            System.out.println("修改订单状态失败");
        }
    }
}

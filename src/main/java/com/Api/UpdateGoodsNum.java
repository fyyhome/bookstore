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

public class UpdateGoodsNum extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        HttpSession session=request.getSession();
        ArrayList<JSONObject> bookArr=(ArrayList<JSONObject>)session.getAttribute("bookArr");
        PrintWriter out=response.getWriter();
        if(bookArr==null)
        {
            out.println(ResJson.generateResJson(-1,"购物车为空", "购物车为空修改操作非法"));
        }
        String sbook_id=request.getParameter("book_id");
        if(sbook_id==null)
        {
            out.println("操作失败,请输入正确的book_id");
            return ;
        }
       int book_id=Integer.parseInt(sbook_id);
        //String sbook_id="100000158";
        //int book_id=100000158;
        String newNum=request.getParameter("book_num");
        if(newNum==null)
        {
            out.println("操作失败,请输入正确的book_num");
            return;
        }
        int num=Integer.parseInt(newNum);
        //String newNum="5";
        //int num=5;
        int flag=0;
        if(num<0)
        {
            out.println(ResJson.generateResJson(-1, "Error", "商品数必须为正整数"));
        }
        else
        {
            for(int i=0;i<bookArr.size();i++)
            {
                JSONObject data=bookArr.get(i);
                int id=data.getInt("book_id");
                if(id==book_id)
                {
                    flag=1;
                    if(num==0)
                    {
                        bookArr.remove(i);
                    }
                    else
                    {
                    JSONObject book=new JSONObject();
                    book.put("book_id",id);
                    book.put("book_num",num);
                    bookArr.set(i,book);
                    break;
                    }
                }
            }
            session.setAttribute("bookArr",bookArr);
        }

    }
}

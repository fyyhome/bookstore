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

public class GoodsNumSub extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        HttpSession session=request.getSession();
        ArrayList<JSONObject> bookArr=(ArrayList<JSONObject>)session.getAttribute("bookArr");
        PrintWriter out=response.getWriter();
        String sbook_id=request.getParameter("book_id");
        int book_id=Integer.parseInt(sbook_id);
        //String sbook_id="100000158";
        //int book_id=100000158;
        for(int i=0;i<bookArr.size();i++)
        {
            JSONObject data=bookArr.get(i);
            int id=data.getInt("book_id");
            if(id==book_id)
            {
                JSONObject book=new JSONObject();
                int book_num=data.getInt("book_num");
                book_num--;
                book.put("book_id",book_id);
                book.put("book_num",book_num);
                bookArr.set(i,book);
                break;
            }
        }
        session.setAttribute("bookArr",bookArr);
    }
}

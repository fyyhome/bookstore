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
import java.util.ArrayList;

public class AddToShopCar extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setCharacterEncoding("UTF-8");
            HttpSession session=request.getSession();//书的信息全部放在bookArr里
           ArrayList<JSONObject> bookArr=(ArrayList<JSONObject>)session.getAttribute("bookArr");
            if(bookArr==null)
            {
                bookArr=new ArrayList<JSONObject>();
            }
            String sbook_id=request.getParameter("book_id");
            if(sbook_id==null)
            {
                response.getWriter().println("添加购物车失败，请输入正确的book_id");
            }
           /* String sbook_id="100000158";
            int book_id=100000158;
            int book_id1=100000611;
            int book_id2=100000810;
            int book_id3=100101360;*/
            int book_id=Integer.parseInt(sbook_id);
            int flag=1;
            for(int i=0;i<bookArr.size();i++)
            {
                JSONObject data=bookArr.get(i);
                int id=data.getInt("book_id");
                if(id==book_id)
                {
                    flag=0;
                    int book_num=data.getInt("book_num");
                    book_num++;
                    JSONObject book=new JSONObject();
                    book.put("book_id",id);
                    book.put("book_num",book_num);
                    bookArr.set(i,book);
                    break;
                }
            }
            if(flag==1)
            {
                JSONObject book=new JSONObject();
                book.put("book_id",book_id);
                book.put("book_num",1);
                bookArr.add(book);
              /*  JSONObject book1=new JSONObject();
                book1.put("book_id",book_id1);
                book1.put("book_num",1);
                bookArr.add(book1);
                JSONObject book2=new JSONObject();
                book2.put("book_id",book_id2);
                book2.put("book_num",1);
                bookArr.add(book2);
                JSONObject book3=new JSONObject();
                book3.put("book_id",book_id3);
                book3.put("book_num",1);
                bookArr.add(book3);*/
            }
            session.setAttribute("bookArr",bookArr);
        }

    }

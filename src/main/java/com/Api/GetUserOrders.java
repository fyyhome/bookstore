package com.Api;

import com.MySql.MysqlUtil;
import com.WebToken.VerifyToken;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

// 登录用户查看自己订单
@WebServlet(name = "GetUserOrders")
public class GetUserOrders extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("")) {
            response.getWriter().println(ResJson.generateResJson(4, "未登录", "未登录"));
        } else {
            if (VerifyToken.myVerify(token)) {
                String user_id = VerifyToken.parseUserId(token);
                String sql = "select * from orders where user_id = " + user_id;
                try {
                    Connection conn = MysqlUtil.getConnection();
                    ResultSet rs = MysqlUtil.excutQuery(conn, sql, null);
                    ArrayList<JSONObject> dataArr = new ArrayList<>();
                    JSONObject order = new JSONObject();
                    float total_price = 0;
                    int order_flag = -1;
                    ArrayList<JSONObject> books = new ArrayList<>();
                    while (rs.next()) {
                        JSONObject book = new JSONObject();
                        int id = rs.getInt("order_id");
                        if (order_flag != id) {
                            if (order_flag != -1) {
                                order.put("books", books);
                                books.clear();
                                dataArr.add(order);
                                total_price = 0;
                            }
                            order_flag = id;
                        }
                        String mysql = "select * from books where book_id = " + rs.getInt("book_id");
                        ResultSet myrs = MysqlUtil.excutQuery(conn, mysql, null);
                        while (myrs.next()) {
                            book.put("book_id", myrs.getInt("book_id"));
                            book.put("book_name", myrs.getString("book_name"));
                            book.put("book_author", myrs.getString("book_author"));
//                        book.put("book_price", myrs.getString("book_price"));
                            book.put("book_publishing", myrs.getString("book_publishing"));
                            book.put("book_smimg", myrs.getString("book_smimg"));
                        }
                        int book_price = myrs.getInt("book_price");
                        total_price += book_price;
                        book.put("book_price", book_price);
                        book.put("book_count",rs.getInt("book_count"));
                        books.add(book);
                        order.put("order_id", id);
                        order.put("is_finsh", rs.getInt("is_finsh"));
                        order.put("total_price", total_price);
                    }
                    response.getWriter().println(ResJson.generateResJson(1, "请求成功", dataArr));
                    MysqlUtil.closeAll(conn,null,rs);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.getWriter().println(ResJson.generateResJson(2, "发生了点意外", "无"));
                }
            } else {
                response.getWriter().println(ResJson.generateResJson(3, "无效token", "无"));
            }
        }
    }
}

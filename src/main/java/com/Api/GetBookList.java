package com.Api;

import com.MySql.MysqlUtil;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

// 书籍分页 page=1&size=12
@WebServlet(name = "GetBookList")
public class GetBookList extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        int page = Integer.parseInt(pageStr);
        int size = Integer.parseInt(sizeStr);
        int start = (page - 1) * size;
        int end = start + size - 1;
        int count = 0;
        String sql = "select * from books";
        try {
            Connection conn = MysqlUtil.getConnection();
            ResultSet rs = MysqlUtil.excutQuery(conn, sql, null);
            ArrayList<JSONObject> bookArr = new ArrayList<>();
            while (rs.next()) {
                if ( count >= start && count <= end) {
                    JSONObject book = new JSONObject();
                    book.put("book_id", rs.getInt("book_id"));
                    book.put("book_name", rs.getString("book_name"));
                    book.put("book_author", rs.getString("book_author"));
                    book.put("book_smimg", rs.getString("book_smimg"));
                    book.put("book_price", rs.getString("book_price"));
                    book.put("book_publishing", rs.getString("book_publishing"));
                    bookArr.add(book);
                }
                count++;
            }
            MysqlUtil.closeAll(conn, conn.prepareStatement(sql), rs);
            response.getWriter().println(ResJson.generateResJson(1, "请求成功", bookArr));
        } catch (Exception e) {
            response.getWriter().println(ResJson.generateResJson(2, e.toString(), e.getMessage()));
            e.getMessage();
        }
    }
}

package com.MySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MysqlUtil {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://120.79.211.126:3306/studyDB?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    static final String USER = "fyy";
    static final String PASS = "123Fuyingyou";

    // 获取连接
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭资源
    public static void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 查询
    public static ResultSet excutQuery(Connection conn, String sql, String[] param) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    pstmt.setString(i+1, param[i]);
                }
            }
            rs = pstmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    // 增删改
    public static int excutUpdate(Connection conn, String sql, String[] param) {
        PreparedStatement pstmt = null;
        int rs = 0;
        try {
            pstmt = conn.prepareStatement(sql);
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    pstmt.setString(i+1, param[i]);
                }
            }
            rs = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstmt, null);
        }
        return rs;
    }
}

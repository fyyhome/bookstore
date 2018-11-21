package com.WebToken;

import com.MySql.MysqlUtil;
import io.jsonwebtoken.Claims;

import java.sql.Connection;
import java.sql.ResultSet;

public class VerifyToken {
    public static boolean myVerify(String token) {
        try {
            Claims c = JJwtUtil.parseJJWT(token);
            String user_id = c.getId();
            if (user_id == null) {
                return false;
            }
            String is_user = null;
            String sql = "select * from user where user_id = " + user_id;
            Connection conn = MysqlUtil.getConnection();
            ResultSet rs = MysqlUtil.excutQuery(conn, sql, null);
            while (rs.next()) {
                is_user = rs.getString("user_id");
            }
            if (is_user == null) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String parseUserId(String token) {
        try {
            Claims c = JJwtUtil.parseJJWT(token);
            String user_id = c.getId();
            return user_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

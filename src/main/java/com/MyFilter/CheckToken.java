package com.MyFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import com.WebToken.JJwtUtil;
import com.Api.ResJson;
import io.jsonwebtoken.Claims;

@WebFilter(filterName = "CheckToken")
public class CheckToken implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        if (req.getAttribute("Authorization") == null) {
            resp.setContentType("application/json");
            resp.getWriter().println(ResJson.generateResJson(1, "未登录", "未登录"));
        } else {
            String tokenStr = (String) req.getAttribute("Authorization");
            try {
                Claims c = JJwtUtil.parseJJWT(tokenStr);
                chain.doFilter(req, resp);

            } catch (Exception e) {
                resp.setContentType("application/json");
                resp.getWriter().println(ResJson.generateResJson(2, "认证已过期", "认证已过期"));
                e.printStackTrace();
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}

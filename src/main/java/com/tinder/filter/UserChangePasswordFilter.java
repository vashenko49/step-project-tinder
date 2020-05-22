package com.tinder.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.model.Error;
import com.tinder.util.FilterUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(urlPatterns = {"*/password"})
public class UserChangePasswordFilter implements Filter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String method = request.getMethod();
            if (method.equals("PUT")) {
                final String JSON = FilterUtil.getStringBody(request).toString();
                Map map = objectMapper.readValue(JSON, Map.class);

                if (map.containsKey("currentPassword") && map.containsKey("newPassword")) {
                    request.setAttribute("userData", map);
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    response.setStatus(422);
                    response.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(422).message("Current password and new password name is require").build()));
                }
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(500).message("Server error").build()));
        }
    }

    @Override
    public void destroy() {

    }
}

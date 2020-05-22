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

@WebFilter(urlPatterns = "*/liked")
public class LikeFilter implements Filter {
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
            final String JSON = FilterUtil.getStringBody(request).toString();
            if (!JSON.isEmpty()) {
                Map map = objectMapper.readValue(JSON, Map.class);
                if (method.equals("GET")) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else if (method.equals("POST")) {
                    if (map.containsKey("partner") && map.containsKey("result")) {
                        request.setAttribute("userData", map);
                        filterChain.doFilter(servletRequest, servletResponse);
                    } else {
                        response.setStatus(422);
                        response.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(422).message("partner and result name is require").build()));
                    }
                }
            }
        } catch (ServletException | IOException e) {
            response.setStatus(500);
            response.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(500).message("Server error").build()));
        }
    }

    @Override
    public void destroy() {

    }
}

package com.tinder.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.exception.ConfigFileException;
import com.tinder.model.Error;
import com.tinder.oauth.UtilJWT;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"*/chat", "*/liked", "*/messages", "*/users", "*/password","*/img"})
public class CheckJwtFilter implements Filter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        servletResponse.setContentType("application/json");
        servletResponse.setCharacterEncoding("UTF-8");
        try {
            final UtilJWT UTIL_JWT = UtilJWT.getInstance();
            String jwt = null;

            for (Cookie cookie : request.getCookies()) {
                if ((cookie == null) || (cookie.getName() == null)) {
                    continue;
                }

                if (cookie.getName().equals("oauth")) {
                    jwt = cookie.getValue();
                }
            }

            if (jwt != null && UTIL_JWT.verifyToken(jwt)) {
                request.setAttribute("jwt", jwt);
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.setStatus(401);
                servletResponse.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(400).message("JWt is not valid").build()));
            }

        } catch (ConfigFileException | SignatureException | ServletException | ExpiredJwtException e) {
            e.printStackTrace();
            response.setStatus(500);
            servletResponse.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(400).message("Parsing error").build()));
        }
    }

    @Override
    public void destroy() {

    }
}

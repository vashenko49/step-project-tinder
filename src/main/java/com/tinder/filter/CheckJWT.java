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
import java.util.Arrays;

@WebFilter(urlPatterns = {"*/chat", "*/liked", "*/messages", "*/users"})
public class CheckJWT implements Filter {
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
            String jwt = Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("oauth"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            if (jwt != null & UTIL_JWT.verifyToken(jwt)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.setStatus(401);
                servletResponse.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(400).message("JWt is not valid").build()));
            }

        } catch (ConfigFileException | SignatureException | ServletException| ExpiredJwtException e) {
            response.setStatus(500);
            servletResponse.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(400).message("Parsing error").build()));
        }
    }

    @Override
    public void destroy() {

    }
}

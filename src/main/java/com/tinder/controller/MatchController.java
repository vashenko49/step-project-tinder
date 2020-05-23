package com.tinder.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.SlideException;
import com.tinder.model.AccountUser;
import com.tinder.model.Error;
import com.tinder.oauth.UtilJWT;
import com.tinder.service.SlideService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/api/v0/match")
public class MatchController extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UtilJWT UTIL_JWT;
    private final SlideService SLIDE_SERVICE;

    public MatchController() throws ConfigFileException, ErrorConnectionToDataBase {
        UTIL_JWT = UtilJWT.getInstance();
        SLIDE_SERVICE = SlideService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final UUID userID = UUID.fromString(UTIL_JWT.getUserFromToken((String) req.getAttribute("jwt")));
        try {
            final List<AccountUser> match = SLIDE_SERVICE.getMatch(userID);
            final String JSON = objectMapper.writeValueAsString(match);
            resp.getWriter().print(JSON);
        } catch (SlideException e) {
            resp.setStatus(500);
            resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(500).message("Error server").build()));
        }
    }
}

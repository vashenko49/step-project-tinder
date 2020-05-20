package com.tinder.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.ImageException;
import com.tinder.exception.UserException;
import com.tinder.model.AccountUser;
import com.tinder.model.Error;
import com.tinder.oauth.UtilJWT;
import com.tinder.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@WebServlet(urlPatterns = "/api/v0/users")
public class UserController extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final UserService instance = UserService.getInstance();
            final UtilJWT UTIL_JWT = UtilJWT.getInstance();
            String jwt = Arrays.stream(req.getCookies())
                    .filter(c -> c.getName().equals("oauth"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            final UUID userID = UUID.fromString(Objects.requireNonNull(UTIL_JWT.getUserFromToken(jwt)));

            final AccountUser userDataByUserId = instance.getUserDataByUserId(userID);

            resp.getWriter().print(objectMapper.writeValueAsString(userDataByUserId));

        } catch (ConfigFileException | ErrorConnectionToDataBase | UserException | ImageException e) {
            resp.setStatus(500);
            resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(400).message("Error get user's data").build()));
        }
    }
}

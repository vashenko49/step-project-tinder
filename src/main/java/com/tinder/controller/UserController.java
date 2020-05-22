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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet(urlPatterns = "/api/v0/users")
public class UserController extends HttpServlet {
    private final UserService instance;
    private final UtilJWT UTIL_JWT;
    public UserController() throws ConfigFileException, ErrorConnectionToDataBase {
        instance = UserService.getInstance();
        UTIL_JWT = UtilJWT.getInstance();
    }


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final UUID userID = UUID.fromString(UTIL_JWT.getUserFromToken((String) req.getAttribute("jwt")));
            final AccountUser userDataByUserId = instance.getUserDataByUserId(userID);
            resp.getWriter().print(objectMapper.writeValueAsString(userDataByUserId));
        } catch (UserException | ImageException |IOException e) {
            resp.setStatus(500);
            resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(400).message("Error get user's data").build()));
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final UUID userID = UUID.fromString(UTIL_JWT.getUserFromToken((String) req.getAttribute("jwt")));
            Map map = (Map) req.getAttribute("userData");
            final AccountUser newDataUSer = instance.editUserData(map, userID);
            if (newDataUSer != null) {
                resp.getWriter().print(objectMapper.writeValueAsString(newDataUSer));
            } else {
                resp.setStatus(400);
                resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(400).message("Error edit user's data").build()));
            }
        } catch (UserException | IOException e) {
            resp.setStatus(500);
            resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(500).message("Server error edit user's data").build()));
        }
    }
}

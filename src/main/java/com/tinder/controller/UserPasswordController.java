package com.tinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.UserException;
import com.tinder.model.Error;
import com.tinder.oauth.UtilJWT;
import com.tinder.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet(urlPatterns = "/api/v0/users/password")
public class UserPasswordController extends HttpServlet {
    private final UserService instance;
    private final UtilJWT UTIL_JWT;

    public UserPasswordController() throws ConfigFileException, ErrorConnectionToDataBase {
        instance = UserService.getInstance();
        UTIL_JWT = UtilJWT.getInstance();
    }


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final UUID userID = UUID.fromString(UTIL_JWT.getUserFromToken((String) req.getAttribute("jwt")));
            Map map = (Map) req.getAttribute("userData");
            final boolean b = instance.changePassword((String) map.get("currentPassword"), (String) map.get("newPassword"), userID);
            if (b) {
                Map<String, String> result = new HashMap<>();
                result.put("message", "Success change password");
                resp.getWriter().print(objectMapper.writeValueAsString(result));
            } else {
                resp.setStatus(401);
                resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(401).message("Password is incorrect").build()));
            }
        } catch (UserException | InvalidKeySpecException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(500).message("Server error change user's password").build()));
        }
    }
}

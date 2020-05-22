package com.tinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.ImageException;
import com.tinder.model.Error;
import com.tinder.oauth.UtilJWT;
import com.tinder.service.UserService;
import com.tinder.util.ControllerUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebServlet(urlPatterns = "/api/v0/users/img")
@MultipartConfig
public class UserImgController extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService instance;
    private final UtilJWT UTIL_JWT;

    public UserImgController() throws ConfigFileException, ErrorConnectionToDataBase {
        instance = UserService.getInstance();
        UTIL_JWT = UtilJWT.getInstance();
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final Part img = req.getPart("img");
            final Part publicIdPart = req.getPart("publicId");
            if (img != null && publicIdPart != null) {
                final String publicId = ControllerUtil.convertInputStreamToString(publicIdPart.getInputStream());
                final UUID userID = UUID.fromString(UTIL_JWT.getUserFromToken((String) req.getAttribute("jwt")));

                final boolean isChange = instance.changeUserImg(img, publicId, userID);

                if (isChange) {
                    Map<String, String> result = new HashMap<>();
                    result.put("message", "Success change img");
                    resp.getWriter().print(objectMapper.writeValueAsString(result));
                } else {
                    resp.setStatus(400);
                    resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(401).message("Failed change img").build()));
                }
            } else {
                resp.setStatus(422);
                resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(422).message("img and publicId name is require").build()));
            }
        } catch (IOException | ServletException | ImageException e) {
            resp.setStatus(500);
            resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(500).message("Error server").build()));
            ;
        }
    }
}

package com.tinder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.defaultImplementation.ImageDefault;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.ImageException;
import com.tinder.exception.UserException;
import com.tinder.model.Error;
import com.tinder.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(urlPatterns = "/api/v0/login")
@MultipartConfig
public class LoginController extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        super.service(req, resp);
    }

    //sign up
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            final UserService USER_SERVICE = UserService.getInstance();
            Part filePart = req.getPart("img");
            final String email = convertInputStreamToString(req.getPart("email").getInputStream());
            final String password = convertInputStreamToString(req.getPart("password").getInputStream());
            final String firstName = convertInputStreamToString(req.getPart("firstName").getInputStream());

            if (USER_SERVICE.userExistByEmail(email)) {
                resp.setStatus(401);
                resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(401).message("Your already have account").build()));
            } else {
                final String JWT = USER_SERVICE.signUpUserFromForm(email, password, firstName, filePart);
                Map<String, String> map = new HashMap<>();
                map.put("jwt", JWT);
                map.put("message", "Success sign up");
                resp.getWriter().print(objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(map));
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | ConfigFileException | ErrorConnectionToDataBase | ImageException | UserException e) {
            resp.setStatus(500);
            resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(400).message("Error sign up user").build()));
        }
    }

    //sign in
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final UserService USER_SERVICE = UserService.getInstance();
            final String email = convertInputStreamToString(req.getPart("email").getInputStream());
            final String password = convertInputStreamToString(req.getPart("password").getInputStream());

            final boolean isExistUser = USER_SERVICE.userExistByEmail(email);

            if (!isExistUser) {
                resp.setStatus(401);
                resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(401).message("You don't have account. Please, Sign Up.").build()));
            } else {
                String JWT = USER_SERVICE.logInUserFromFrom(email, password);
                if (JWT.isEmpty()) {
                    resp.setStatus(401);
                    resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(401).message("Password incorrect").build()));
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("jwt", JWT);
                    map.put("message", "Success sign in");
                    resp.getWriter().print(objectMapper.writerWithDefaultPrettyPrinter()
                            .writeValueAsString(map));
                }
            }

        } catch (ErrorConnectionToDataBase | ConfigFileException | ServletException | UserException | InvalidKeySpecException | NoSuchAlgorithmException e) {
            resp.setStatus(500);
            resp.getWriter().print(objectMapper.writeValueAsString(Error.builder().status(400).message("Error sign in user").build()));
        }
    }


    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        return result.toString(StandardCharsets.UTF_8.name());

    }

}

package com.tinder.controller;

import com.tinder.exception.*;
import com.tinder.service.GoogleService;
import com.tinder.util.GoogleUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = "/api/v0/google")
public class GoogleController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            final GoogleService GOOGLE_SERVICE = GoogleService.getInstance();

            //code to get a client access token
            String code = req.getParameter("code");

            //get user scope profile and email
            final Map<String, String> userDataUseCode = GoogleUtil.getUserDataUseCode(code);

            if (GOOGLE_SERVICE.userExistByEmail(userDataUseCode.get("email"))) {
                resp.sendRedirect("http://localhost:3000/sing-in");
            } else {
                final String JWT = GOOGLE_SERVICE.SingUpUserByGoogle(userDataUseCode);
                resp.sendRedirect("http://localhost:3000?oauth=" + JWT);
            }

        } catch (ConfigFileException | GoogleException | ImageException | UserException | ErrorConnectionToDataBase e) {
            resp.sendRedirect("http://localhost:3000/error?error=Error%20during%20registration%2C%20via%20google.%20Try%20later");
        }
    }
}

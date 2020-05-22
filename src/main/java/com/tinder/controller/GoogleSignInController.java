package com.tinder.controller;

import com.tinder.exception.*;
import com.tinder.service.GoogleSignInService;
import com.tinder.util.GoogleUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/api/v0/google-sign-in")
public class GoogleSignInController extends HttpServlet {
    private final GoogleSignInService GOOGLE_SERVICE;

    public GoogleSignInController() throws ErrorConnectionToDataBase {
        GOOGLE_SERVICE = GoogleSignInService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = req.getParameter("code");
            if (code == null || code.isEmpty()) {
                resp.sendRedirect("http://localhost:3000/error?error=Google%20Access%20Issues");
            } else {
                String userEmail = GoogleUtil.getEmailDataUseCode(code);
                UUID userId = GOOGLE_SERVICE.getUserUUIDByEmail(userEmail);
                if (userId == null) {
                    resp.sendRedirect("http://localhost:3000/sing-up");
                } else {
                    String JWT = GOOGLE_SERVICE.singInUserInSystem(userId);
                    resp.sendRedirect("http://localhost:3000?oauth=" + JWT);
                }
            }

        } catch (ConfigFileException | GoogleException  | UserException e) {
            System.out.println(e);
            resp.sendRedirect("http://localhost:3000/error?error=Error%20during%20registration%2C%20via%20google.%20Try%20later");
        }
    }
}

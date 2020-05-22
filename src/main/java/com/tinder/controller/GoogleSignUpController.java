package com.tinder.controller;

import com.tinder.exception.*;
import com.tinder.service.GoogleSignInService;
import com.tinder.service.GoogleSignUpService;
import com.tinder.util.GoogleUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(urlPatterns = "/api/v0/google-sign-up")
public class GoogleSignUpController extends HttpServlet {
    private final GoogleSignUpService GOOGLE_SERVICE;

    public GoogleSignUpController() throws ConfigFileException, ErrorConnectionToDataBase {
        GOOGLE_SERVICE = GoogleSignUpService.getInstance();
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            //code to get a client access token
            String code = req.getParameter("code");

            //get user scope profile and email
            final Map<String, String> userDataUseCode = GoogleUtil.getUserDataUseCodeProfileAndEmail(code);

            if (GOOGLE_SERVICE.userExistByEmail(userDataUseCode.get("email"))) {
                resp.sendRedirect("http://localhost:3000/sing-in?message=Your%20already%20have%20account");
            } else {
                final String JWT = GOOGLE_SERVICE.SingUpUserByGoogle(userDataUseCode);
                resp.sendRedirect("http://localhost:3000?oauth=" + JWT);
            }

        } catch (ConfigFileException | GoogleException | ImageException | UserException  e) {
            System.out.println(e);
            resp.sendRedirect("http://localhost:3000/error?error=Error%20during%20registration%2C%20via%20google.%20Try%20later");
        }
    }
}

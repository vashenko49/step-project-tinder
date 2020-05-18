package com.tinder.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.defaultImplementation.ImageDefault;
import com.tinder.defaultImplementation.UserDefault;
import com.tinder.exception.*;
import com.tinder.oauth.UtilJWT;
import com.tinder.start.ConfigFile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GoogleService {
    private static volatile GoogleService instance;
    private final UserDefault USER_DEFAULT;
    private final ImageDefault IMAGE_DEFAULT;

    private GoogleService() throws ErrorConnectionToDataBase, ConfigFileException {
        USER_DEFAULT = UserDefault.getInstance();
        IMAGE_DEFAULT = ImageDefault.getInstance();
    }

    public static GoogleService getInstance() throws ConfigFileException, ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (GoogleService.class) {
                if (instance == null) {
                    instance = new GoogleService();
                }
            }
        }
        return instance;
    }


    final ObjectMapper objectMapper = new ObjectMapper();

    public String SingUpUserByGoogle(Map<String, String> userData) throws ImageException, ConfigFileException, UserException {
        final UUID userFromGoogle = USER_DEFAULT.createUserFromGoogle(userData.get("firstName"), userData.get("email"));
        final String publicId = IMAGE_DEFAULT.uploadImgUseUri(userData.get("imgUrl"));
        IMAGE_DEFAULT.saveImgUrlInDataBaseByUserID(userFromGoogle, publicId);
        return UtilJWT.getInstance().createSingToken(userFromGoogle.toString());
    }

    public boolean userExistByEmail(String email) throws UserException {
        return USER_DEFAULT.userExistByEmail(email);
    }
}

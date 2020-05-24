package com.tinder.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinder.defaultImplementation.ImageDefault;
import com.tinder.defaultImplementation.UserDefault;
import com.tinder.exception.*;
import com.tinder.oauth.UtilJWT;

import java.util.Map;
import java.util.UUID;

public final class GoogleSignUpService {
    private static volatile GoogleSignUpService instance;
    private final UserDefault USER_DEFAULT;
    private final ImageDefault IMAGE_DEFAULT;

    private GoogleSignUpService() throws ErrorConnectionToDataBase, ConfigFileException {
        USER_DEFAULT = UserDefault.getInstance();
        IMAGE_DEFAULT = ImageDefault.getInstance();
    }

    public static GoogleSignUpService getInstance() throws ConfigFileException, ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (GoogleSignUpService.class) {
                if (instance == null) {
                    instance = new GoogleSignUpService();
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

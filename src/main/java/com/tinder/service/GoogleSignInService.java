package com.tinder.service;

import com.tinder.defaultImplementation.UserDefault;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.UserException;
import com.tinder.oauth.UtilJWT;

import java.util.UUID;

public final class GoogleSignInService {
    private static volatile GoogleSignInService instance;
    private final UserDefault USER_DEFAULT;

    private GoogleSignInService() throws ErrorConnectionToDataBase {
        this.USER_DEFAULT = UserDefault.getInstance();
    }

    public static GoogleSignInService getInstance() throws ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (GoogleSignInService.class) {
                if (instance == null) {
                    instance = new GoogleSignInService();
                }
            }
        }
        return instance;
    }

    public UUID getUserUUIDByEmail(String email) throws UserException {
        return USER_DEFAULT.getUserUUIDByEmail(email);
    }

    public String singInUserInSystem(UUID userId) throws ConfigFileException {
        return UtilJWT.getInstance().createSingToken(userId.toString());
    }
}

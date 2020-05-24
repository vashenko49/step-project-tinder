package com.tinder.service;

import com.tinder.defaultImplementation.ImageDefault;
import com.tinder.defaultImplementation.UserDefault;
import com.tinder.exception.ConfigFileException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.ImageException;
import com.tinder.exception.UserException;
import com.tinder.model.AccountUser;
import com.tinder.oauth.SecuredPassword;
import com.tinder.oauth.UtilJWT;
import javafx.util.Pair;

import javax.servlet.http.Part;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.UUID;

public final class UserService {
    private static volatile UserService instance;
    private final UserDefault USER_DEFAULT;
    private final ImageDefault IMAGE_DEFAULT;

    private UserService() throws ErrorConnectionToDataBase, ConfigFileException {
        this.USER_DEFAULT = UserDefault.getInstance();
        this.IMAGE_DEFAULT = ImageDefault.getInstance();
    }

    public static UserService getInstance() throws ErrorConnectionToDataBase, ConfigFileException {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }


    public AccountUser getUserDataByUserId(UUID userId) throws UserException, ImageException {
        return USER_DEFAULT.getUserDataByUserId(userId);
    }

    public boolean userExistByEmail(String email) throws UserException {
        return USER_DEFAULT.userExistByEmail(email);
    }

    public String signUpUserFromForm(String email, String password, String firstName, Part file) throws InvalidKeySpecException, NoSuchAlgorithmException, UserException, ImageException, ConfigFileException {
        final UUID userFromForm = USER_DEFAULT.createUserFromForm(email, SecuredPassword.generateStrongPasswordHash(password), firstName);
        IMAGE_DEFAULT.saveImgUrlInDataBaseByUserID(userFromForm, IMAGE_DEFAULT.uploadImg(file));
        return UtilJWT.getInstance().createSingToken(userFromForm.toString());
    }

    public String logInUserFromFrom(String email, String password) throws UserException, InvalidKeySpecException, NoSuchAlgorithmException, ConfigFileException {
        final Pair<String, String> userData = USER_DEFAULT.getUserIdAndPasswordByEmail(email);
        if (userData.getValue() != null && userData.getKey() != null) {
            final boolean isValid = SecuredPassword.validatePassword(password, userData.getValue());
            if (isValid) {
                return UtilJWT.getInstance().createSingToken(userData.getKey());
            }
        }
        return "";
    }

    public AccountUser editUserData(Map newData, UUID userId) throws UserException {
        return USER_DEFAULT.editUserData(newData, userId);
    }

    public boolean changePassword(String currentPassword, String newPassword, UUID userId) throws UserException, InvalidKeySpecException, NoSuchAlgorithmException {
        String currentPasswordFromDB = USER_DEFAULT.getCurrentPasswordByUserId(userId);
        if (currentPasswordFromDB != null) {
            final boolean isValid = SecuredPassword.validatePassword(currentPassword, currentPasswordFromDB);
            if (isValid) {
                return USER_DEFAULT.changePassword(SecuredPassword.generateStrongPasswordHash(newPassword), userId);
            }
        }
        return false;
    }


    public boolean changeUserImg(Part img, String publicId, UUID userId) throws ImageException {
        if (IMAGE_DEFAULT.dropImgByPublicId(publicId) & IMAGE_DEFAULT.dropImgUrlFromDataBaseByPublicId(publicId)) {
            return IMAGE_DEFAULT.saveImgUrlInDataBaseByUserID(userId, IMAGE_DEFAULT.uploadImg(img));
        }
        return false;
    }
}

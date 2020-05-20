package com.tinder.dao;

import com.tinder.exception.ImageException;
import com.tinder.exception.UserException;
import com.tinder.model.AccountUser;
import javafx.util.Pair;

import java.util.UUID;

public interface UserDAO {
    UUID createUserFromForm(String email, String password, String firstName) throws UserException;

    UUID createUserFromGoogle(String firstName, String email) throws UserException;

    boolean dropUser(UUID userID) throws UserException;

    boolean userExistByEmail(String email) throws UserException;

    UUID getUserUUIDByEmail(String email) throws UserException;

    AccountUser getUserDataByUserId(UUID userId) throws UserException, ImageException;

    Pair<String, String> getUserIdAndPasswordByEmail(String email) throws UserException;
}

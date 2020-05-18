package com.tinder.dao;

import com.tinder.exception.UserException;
import com.tinder.model.AccountTinder;

import java.util.List;
import java.util.UUID;

public interface UserDAO {
    UUID createUserFromForm(AccountTinder accountTinder);

    UUID createUserFromGoogle(String firstName, String email) throws UserException;

    boolean editUser(AccountTinder accountTinder);

    boolean dropUser(UUID userID) throws UserException;

    AccountTinder getUserData(String email);

    List<AccountTinder> getListUsersByUser(AccountTinder accountTinder);

    boolean userExistByEmail(String email) throws UserException;
}

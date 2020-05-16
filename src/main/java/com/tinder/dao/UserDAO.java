package com.tinder.dao;

import com.tinder.model.AccountTinder;

import java.util.List;
import java.util.UUID;

public interface UserDAO {
    boolean createUser(AccountTinder accountTinder);

    boolean editUser(AccountTinder accountTinder);

    boolean dropUser(UUID userID);

    AccountTinder getUserData(String email);

    List<AccountTinder> getListUsersByUser(AccountTinder accountTinder);
}

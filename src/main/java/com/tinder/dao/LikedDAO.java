package com.tinder.dao;

import com.tinder.model.AccountTinder;

import java.util.UUID;

public interface LikedDAO {
    boolean setLike(UUID from, UUID to);

    AccountTinder getMatch(UUID from, UUID to);
}

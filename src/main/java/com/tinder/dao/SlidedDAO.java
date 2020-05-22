package com.tinder.dao;

import com.tinder.exception.SlideException;
import com.tinder.model.AccountUser;

import java.util.List;
import java.util.UUID;

public interface SlidedDAO {
    List<AccountUser> getPackUserForLike(UUID userId) throws SlideException;

    List<AccountUser> getMatch(UUID userId) throws SlideException;

    boolean slideAndIsMatch(UUID userId, UUID partner, boolean result) throws SlideException;

}

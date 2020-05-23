package com.tinder.service;

import com.tinder.defaultImplementation.SlideDefault;

import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.SlideException;
import com.tinder.model.AccountUser;

import java.util.List;
import java.util.UUID;

public class SlideService {
    private static volatile SlideService instance;
    private static SlideDefault SLIDE_DEFAULT;

    private SlideService() throws ErrorConnectionToDataBase {
        SLIDE_DEFAULT = SlideDefault.getInstance();
    }

    public static SlideService getInstance() throws ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (SlideService.class) {
                if (instance == null) {
                    instance = new SlideService();
                }
            }
        }
        return instance;
    }

    public boolean slideAndIsMatch(UUID userId, UUID partner, boolean result) throws SlideException {
        return SLIDE_DEFAULT.slideAndIsMatch(userId, partner, result);
    }

    public List<AccountUser> getPackUserForLike(UUID userId) throws SlideException {
        return SLIDE_DEFAULT.getPackUserForLike(userId);
    }

    public List<AccountUser> getMatch(UUID userId) throws SlideException {
        return SLIDE_DEFAULT.getMatch(userId);
    }
}

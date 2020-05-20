package com.tinder.dao;

import java.util.UUID;

public interface LikedDAO {
    boolean setLike(UUID from, UUID to);

}

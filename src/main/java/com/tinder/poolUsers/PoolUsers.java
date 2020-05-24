package com.tinder.poolUsers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class PoolUsers {
    private static volatile PoolUsers instance;
    private final Map<String, OperationWithUser> poolUsers = new ConcurrentHashMap<>();

    public PoolUsers() {

    }

    public static PoolUsers getInstance() {
        if (instance == null) {
            synchronized (PoolUsers.class) {
                if (instance == null) {
                    instance = new PoolUsers();
                }
            }
        }
        return instance;
    }

    public boolean containUser(String userId) {
        return poolUsers.containsKey(userId);
    }

    public OperationWithUser getOperationByUserId(String userId) {
        return poolUsers.get(userId);
    }

    public OperationWithUser dropUserFromPool(String userId) {
        return poolUsers.remove(userId);
    }

    public OperationWithUser addToPoolUser(String userId, OperationWithUser operation) {
        return poolUsers.put(userId, operation);
    }
}

package com.tinder.service;

public final class MessagesService {
    private static volatile MessagesService instance;
    private MessagesService() {

    }

    public static MessagesService getInstance() {
        if (instance == null) {
            synchronized (MessagesService.class) {
                if (instance == null) {
                    instance = new MessagesService();
                }
            }
        }
        return instance;
    }
}

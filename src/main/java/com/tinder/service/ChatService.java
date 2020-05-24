package com.tinder.service;


public final class ChatService {
    private static volatile ChatService instance;
    private ChatService() {

    }

    public static ChatService getInstance() {
        if (instance == null) {
            synchronized (ChatService.class) {
                if (instance == null) {
                    instance = new ChatService();
                }
            }
        }
        return instance;
    }
}

package com.tinder.service;


import com.tinder.defaultImplementation.ChatDefault;
import com.tinder.exception.ChatException;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.model.Chat;

import java.util.List;
import java.util.UUID;

public final class ChatService {
    private static volatile ChatService instance;
    private final ChatDefault CHAT_DEFAULT;

    private ChatService() throws ErrorConnectionToDataBase {
        CHAT_DEFAULT = ChatDefault.getInstance();
    }

    public static ChatService getInstance() throws ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (ChatService.class) {
                if (instance == null) {
                    instance = new ChatService();
                }
            }
        }
        return instance;
    }

    public boolean isChatWithPartner(UUID user, UUID partner) throws ChatException {
        return CHAT_DEFAULT.isChatWithPartner(user, partner);
    }

    public UUID createChat(UUID user, UUID partner) throws ChatException {
        return CHAT_DEFAULT.createChat(user, partner);
    }

    public boolean makeChatReadForUser(UUID userId, UUID chatId) throws ChatException {
        return CHAT_DEFAULT.makeChatReadForUser(userId, chatId);
    }

    public List<Chat> getUserChats(UUID user) throws ChatException {
        return CHAT_DEFAULT.getUserChats(user);
    }

    public boolean dropChat(UUID chatId) throws ChatException {
        return CHAT_DEFAULT.dropChat(chatId);
    }
}

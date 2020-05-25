package com.tinder.service;

import com.tinder.defaultImplementation.MessagesDefault;
import com.tinder.exception.ErrorConnectionToDataBase;
import com.tinder.exception.MessagesException;
import com.tinder.model.Message;

import java.util.List;
import java.util.UUID;

public final class MessagesService {
    private static volatile MessagesService instance;
    private final MessagesDefault MESSAGES_DEFAULT;

    private MessagesService() throws ErrorConnectionToDataBase {
        MESSAGES_DEFAULT = MessagesDefault.getInstance();
    }

    public static MessagesService getInstance() throws ErrorConnectionToDataBase {
        if (instance == null) {
            synchronized (MessagesService.class) {
                if (instance == null) {
                    instance = new MessagesService();
                }
            }
        }
        return instance;
    }

    public boolean deleteMessage(UUID messageId) throws MessagesException {
        return MESSAGES_DEFAULT.deleteMessage(messageId);
    }

    public boolean sendMessage(UUID chatId, UUID userId, UUID receiver, String textMessage) throws MessagesException {
        return MESSAGES_DEFAULT.sendMessage(chatId, userId, receiver, textMessage);
    }

    public List<Message> getMessagesForChat(UUID chatId, int page) throws MessagesException {
        return MESSAGES_DEFAULT.getMessagesForChat(chatId, page);
    }
}

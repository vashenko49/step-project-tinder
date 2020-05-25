package com.tinder.dao;

import com.tinder.exception.MessagesException;
import com.tinder.model.Message;


import java.util.List;
import java.util.UUID;

public interface MessagesDAO {
    List<Message> getMessagesForChat(UUID chatId, int page) throws MessagesException;

    boolean deleteMessage(UUID messageId) throws MessagesException;

    boolean sendMessage(UUID chatId, UUID userId, UUID receiver, String textMessage) throws MessagesException;
}

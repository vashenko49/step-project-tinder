package com.tinder.dao;

import com.tinder.exception.ChatException;
import com.tinder.model.Chat;

import java.util.List;
import java.util.UUID;

public interface ChatDAO {
    boolean isChatWithPartner(UUID user, UUID partner) throws ChatException;

    UUID createChat(UUID user, UUID partner) throws ChatException;

    List<Chat> getUserChats(UUID user) throws ChatException;

    boolean dropChat(UUID chatId) throws ChatException;

    boolean makeChatReadForUser(UUID userId, UUID chatId) throws ChatException;

}

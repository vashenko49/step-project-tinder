package com.tinder.poolUsers;

import com.tinder.exception.ChatException;
import com.tinder.exception.MessagesException;

import java.io.IOException;

@FunctionalInterface
public interface OperationMessage {
    void operation(int page) throws IOException, ChatException, MessagesException;
}

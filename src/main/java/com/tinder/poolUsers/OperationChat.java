package com.tinder.poolUsers;

import com.tinder.exception.ChatException;
import com.tinder.exception.MessagesException;

import java.io.IOException;

@FunctionalInterface
public interface OperationChat {
    void operation() throws IOException, ChatException, MessagesException;
}

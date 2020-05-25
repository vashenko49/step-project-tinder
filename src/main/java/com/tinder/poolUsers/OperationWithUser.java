package com.tinder.poolUsers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationWithUser {
    private String activeChat;
    final private OperationChat updateСhat;
    private OperationMessage updateMessage;
}

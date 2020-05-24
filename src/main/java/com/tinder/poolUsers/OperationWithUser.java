package com.tinder.poolUsers;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OperationWithUser {
    private Operation updateChat;
    private Operation updateMessages;
}

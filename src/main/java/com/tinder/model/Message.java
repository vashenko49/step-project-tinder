package com.tinder.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Message {
    private final UUID messages_id;
    private final String from_id;
    private final String tou_id;
    private final String chat_id;
    private final boolean read;
    private final String message_text;
    private final Date time_send;
}

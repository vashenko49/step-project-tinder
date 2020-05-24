package com.tinder.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class Chat {
    private final UUID chat_id;
    private final String tou_id;
    private final String tou_name;
    private final String tou_img_url;
    private final String from_id;
    private final String from_name;
    private final String from_img_url;
    private final String message_text;
    private final Date time_send;
    private final int number_unread;
}

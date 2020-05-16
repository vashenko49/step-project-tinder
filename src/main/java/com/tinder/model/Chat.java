package com.tinder.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Chat {
    private final UUID chatid;
    private final String nameFrom;
    private final String lastTextMessage;
}

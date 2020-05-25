package com.tinder.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Socket {
    private final String type;
    private final String message;
}

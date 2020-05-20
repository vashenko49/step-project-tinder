package com.tinder.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Error {
    private final int status;
    private final String message;
}

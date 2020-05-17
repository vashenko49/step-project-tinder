package com.tinder.exception;

public class ErrorConnectionToDataBase extends Throwable {
    public ErrorConnectionToDataBase(String message) {
        super(message);
    }
}

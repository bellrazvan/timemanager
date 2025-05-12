package com.time.timemanager.config.exceptions;

public class AccountUnconfirmedException extends RuntimeException {
    public AccountUnconfirmedException(String message) {
        super(message);
    }
}
package com.time.timemanager.authentication.exceptions;

public class AccountUnconfirmedException extends RuntimeException {
    public AccountUnconfirmedException(String message) {
        super(message);
    }
}
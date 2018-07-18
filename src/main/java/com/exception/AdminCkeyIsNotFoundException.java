package com.exception;

public class AdminCkeyIsNotFoundException extends Exception {
    public AdminCkeyIsNotFoundException() {
    }

    public AdminCkeyIsNotFoundException(String message) {
        super(message);
    }

    public AdminCkeyIsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdminCkeyIsNotFoundException(Throwable cause) {
        super(cause);
    }

    public AdminCkeyIsNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

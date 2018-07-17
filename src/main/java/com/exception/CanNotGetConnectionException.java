package com.exception;

public class CanNotGetConnectionException extends RuntimeException {
    public CanNotGetConnectionException() {
        super();
    }

    public CanNotGetConnectionException(String message) {
        super(message);
    }

    public CanNotGetConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotGetConnectionException(Throwable cause) {
        super(cause);
    }

    protected CanNotGetConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

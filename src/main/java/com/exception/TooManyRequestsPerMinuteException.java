package com.exception;

public class TooManyRequestsPerMinuteException extends Exception {
    public TooManyRequestsPerMinuteException() {
    }

    public TooManyRequestsPerMinuteException(String message) {
        super(message);
    }

    public TooManyRequestsPerMinuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyRequestsPerMinuteException(Throwable cause) {
        super(cause);
    }

    public TooManyRequestsPerMinuteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

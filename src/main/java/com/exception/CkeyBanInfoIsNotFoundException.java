package com.exception;

public class CkeyBanInfoIsNotFoundException extends Exception {
    public CkeyBanInfoIsNotFoundException() {
    }

    public CkeyBanInfoIsNotFoundException(String message) {
        super(message);
    }

    public CkeyBanInfoIsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CkeyBanInfoIsNotFoundException(Throwable cause) {
        super(cause);
    }

    public CkeyBanInfoIsNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

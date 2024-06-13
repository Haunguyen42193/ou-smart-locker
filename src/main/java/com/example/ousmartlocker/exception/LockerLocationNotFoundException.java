package com.example.ousmartlocker.exception;

public class LockerLocationNotFoundException extends OuSmartLockerBadRequestApiException {
    public LockerLocationNotFoundException() {
    }

    public LockerLocationNotFoundException(String message) {
        super(message);
    }

    public LockerLocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockerLocationNotFoundException(Throwable cause) {
        super(cause);
    }

    public LockerLocationNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

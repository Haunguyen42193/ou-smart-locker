package com.example.ousmartlocker.exception;

public class LockerNotFoundException extends OuSmartLockerBadRequestApiException {
    public LockerNotFoundException() {
    }

    public LockerNotFoundException(String message) {
        super(message);
    }

    public LockerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockerNotFoundException(Throwable cause) {
        super(cause);
    }

    public LockerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

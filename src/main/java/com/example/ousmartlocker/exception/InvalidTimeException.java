package com.example.ousmartlocker.exception;

public class InvalidTimeException extends OuSmartLockerBadRequestApiException{
    public InvalidTimeException() {
    }

    public InvalidTimeException(String message) {
        super(message);
    }

    public InvalidTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidTimeException(Throwable cause) {
        super(cause);
    }

    public InvalidTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

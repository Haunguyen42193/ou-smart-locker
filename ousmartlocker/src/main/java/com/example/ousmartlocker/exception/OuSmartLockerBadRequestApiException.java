package com.example.ousmartlocker.exception;

public class OuSmartLockerBadRequestApiException extends RuntimeException{
    public OuSmartLockerBadRequestApiException() {
        super();
    }

    public OuSmartLockerBadRequestApiException(String message) {
        super(message);
    }

    public OuSmartLockerBadRequestApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public OuSmartLockerBadRequestApiException(Throwable cause) {
        super(cause);
    }

    public OuSmartLockerBadRequestApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

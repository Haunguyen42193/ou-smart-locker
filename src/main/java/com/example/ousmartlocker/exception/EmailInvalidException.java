package com.example.ousmartlocker.exception;

public class EmailInvalidException extends OuSmartLockerBadRequestApiException{
    public EmailInvalidException() {
    }

    public EmailInvalidException(String message) {
        super(message);
    }

    public EmailInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailInvalidException(Throwable cause) {
        super(cause);
    }

    public EmailInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

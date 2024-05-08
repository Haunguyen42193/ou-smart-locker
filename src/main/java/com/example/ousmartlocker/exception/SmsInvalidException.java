package com.example.ousmartlocker.exception;

public class SmsInvalidException extends OuSmartLockerBadRequestApiException{
    public SmsInvalidException() {
    }

    public SmsInvalidException(String message) {
        super(message);
    }

    public SmsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmsInvalidException(Throwable cause) {
        super(cause);
    }

    public SmsInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

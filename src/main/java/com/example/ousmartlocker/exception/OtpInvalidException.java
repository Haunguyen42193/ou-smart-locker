package com.example.ousmartlocker.exception;

public class OtpInvalidException extends OuSmartLockerBadRequestApiException{
    public OtpInvalidException() {
    }

    public OtpInvalidException(String message) {
        super(message);
    }

    public OtpInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public OtpInvalidException(Throwable cause) {
        super(cause);
    }

    public OtpInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

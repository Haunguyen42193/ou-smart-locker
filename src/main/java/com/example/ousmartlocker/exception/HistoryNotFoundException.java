package com.example.ousmartlocker.exception;

public class HistoryNotFoundException extends OuSmartLockerBadRequestApiException{
    public HistoryNotFoundException() {
    }

    public HistoryNotFoundException(String message) {
        super(message);
    }

    public HistoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public HistoryNotFoundException(Throwable cause) {
        super(cause);
    }

    public HistoryNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

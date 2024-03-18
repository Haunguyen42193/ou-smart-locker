package com.example.ousmartlocker.exception;

public class HistoryInvalidException extends OuSmartLockerBadRequestApiException{
    public HistoryInvalidException() {
    }

    public HistoryInvalidException(String message) {
        super(message);
    }

    public HistoryInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public HistoryInvalidException(Throwable cause) {
        super(cause);
    }

    public HistoryInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

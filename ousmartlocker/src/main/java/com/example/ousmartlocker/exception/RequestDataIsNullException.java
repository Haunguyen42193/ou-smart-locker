package com.example.ousmartlocker.exception;

public class RequestDataIsNullException extends OuSmartLockerBadRequestApiException {
    public RequestDataIsNullException() {
    }

    public RequestDataIsNullException(String message) {
        super(message);
    }

    public RequestDataIsNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestDataIsNullException(Throwable cause) {
        super(cause);
    }

    public RequestDataIsNullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

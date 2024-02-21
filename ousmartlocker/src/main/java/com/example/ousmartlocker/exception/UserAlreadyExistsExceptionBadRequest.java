package com.example.ousmartlocker.exception;

public class UserAlreadyExistsExceptionBadRequest extends OuSmartLockerBadRequestApiException {
    public UserAlreadyExistsExceptionBadRequest() {
    }

    public UserAlreadyExistsExceptionBadRequest(String message) {
        super(message);
    }

    public UserAlreadyExistsExceptionBadRequest(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsExceptionBadRequest(Throwable cause) {
        super(cause);
    }

    public UserAlreadyExistsExceptionBadRequest(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

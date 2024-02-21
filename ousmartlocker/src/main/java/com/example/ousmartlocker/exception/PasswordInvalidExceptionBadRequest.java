package com.example.ousmartlocker.exception;

public class PasswordInvalidExceptionBadRequest extends OuSmartLockerBadRequestApiException {
    public PasswordInvalidExceptionBadRequest() {
    }

    public PasswordInvalidExceptionBadRequest(String message) {
        super(message);
    }

    public PasswordInvalidExceptionBadRequest(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordInvalidExceptionBadRequest(Throwable cause) {
        super(cause);
    }

    public PasswordInvalidExceptionBadRequest(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

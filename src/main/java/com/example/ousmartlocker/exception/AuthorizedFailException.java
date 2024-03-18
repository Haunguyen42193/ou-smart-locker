package com.example.ousmartlocker.exception;

public class AuthorizedFailException extends OuSmartLockerUnauthorizedApiException{
    public AuthorizedFailException() {
    }

    public AuthorizedFailException(String message) {
        super(message);
    }

    public AuthorizedFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizedFailException(Throwable cause) {
        super(cause);
    }

    public AuthorizedFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

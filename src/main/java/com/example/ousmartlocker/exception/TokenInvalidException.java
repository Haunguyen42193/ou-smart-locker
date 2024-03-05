package com.example.ousmartlocker.exception;

public class TokenInvalidException extends OuSmartLockerUnauthorizedApiException{
    public TokenInvalidException() {
    }

    public TokenInvalidException(String message) {
        super(message);
    }

    public TokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenInvalidException(Throwable cause) {
        super(cause);
    }

    public TokenInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

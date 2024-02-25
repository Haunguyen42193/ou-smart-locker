package com.example.ousmartlocker.exception;

public class UsernamePasswordInvalid extends OuSmartLockerUnauthorizedApiException{
    public UsernamePasswordInvalid() {
    }

    public UsernamePasswordInvalid(String message) {
        super(message);
    }

    public UsernamePasswordInvalid(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernamePasswordInvalid(Throwable cause) {
        super(cause);
    }

    public UsernamePasswordInvalid(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

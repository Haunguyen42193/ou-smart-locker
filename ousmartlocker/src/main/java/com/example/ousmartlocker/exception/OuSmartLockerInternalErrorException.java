package com.example.ousmartlocker.exception;

public class OuSmartLockerInternalErrorException extends RuntimeException{
    public OuSmartLockerInternalErrorException() {
    }

    public OuSmartLockerInternalErrorException(String message) {
        super(message);
    }

    public OuSmartLockerInternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public OuSmartLockerInternalErrorException(Throwable cause) {
        super(cause);
    }

    public OuSmartLockerInternalErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package com.example.ousmartlocker.exception;

public class PersistenceDataFailException extends RuntimeException{
    public PersistenceDataFailException() {
    }

    public PersistenceDataFailException(String message) {
        super(message);
    }

    public PersistenceDataFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceDataFailException(Throwable cause) {
        super(cause);
    }

    public PersistenceDataFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

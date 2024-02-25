package com.example.ousmartlocker.exception;

public class OuSmartLockerUnauthorizedApiException extends RuntimeException {
    public OuSmartLockerUnauthorizedApiException() {
    }

    public OuSmartLockerUnauthorizedApiException(String message) {
        super(message);
    }

    public OuSmartLockerUnauthorizedApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public OuSmartLockerUnauthorizedApiException(Throwable cause) {
        super(cause);
    }

    public OuSmartLockerUnauthorizedApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

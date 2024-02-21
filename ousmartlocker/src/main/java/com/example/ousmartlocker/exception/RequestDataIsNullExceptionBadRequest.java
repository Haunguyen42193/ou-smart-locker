package com.example.ousmartlocker.exception;

public class RequestDataIsNullExceptionBadRequest extends OuSmartLockerBadRequestApiException {
    public RequestDataIsNullExceptionBadRequest() {
    }

    public RequestDataIsNullExceptionBadRequest(String message) {
        super(message);
    }

    public RequestDataIsNullExceptionBadRequest(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestDataIsNullExceptionBadRequest(Throwable cause) {
        super(cause);
    }

    public RequestDataIsNullExceptionBadRequest(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

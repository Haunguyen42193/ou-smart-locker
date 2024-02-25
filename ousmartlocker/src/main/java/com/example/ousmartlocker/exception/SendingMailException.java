package com.example.ousmartlocker.exception;

public class SendingMailException extends OuSmartLockerInternalErrorException{
    public SendingMailException() {
    }

    public SendingMailException(String message) {
        super(message);
    }

    public SendingMailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendingMailException(Throwable cause) {
        super(cause);
    }

    public SendingMailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

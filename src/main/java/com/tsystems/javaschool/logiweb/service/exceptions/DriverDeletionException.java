package com.tsystems.javaschool.logiweb.service.exceptions;

public class DriverDeletionException extends LogiwebServiceException {

    public DriverDeletionException() {
    }

    public DriverDeletionException(String message) {
        super(message);
    }

    public DriverDeletionException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DriverDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverDeletionException(Throwable cause) {
        super(cause);
    }

}

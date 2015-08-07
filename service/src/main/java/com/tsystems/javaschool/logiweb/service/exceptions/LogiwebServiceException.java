package com.tsystems.javaschool.logiweb.service.exceptions;

public class LogiwebServiceException extends Exception {
    
    public LogiwebServiceException() {
        super();
    }

    public LogiwebServiceException(String message) {
        super(message);
    }

    public LogiwebServiceException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public LogiwebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogiwebServiceException(Throwable cause) {
        super(cause);
    }
}

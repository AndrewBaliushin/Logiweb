package com.tsystems.javaschool.logiweb.service.exceptions;

public class UserNotFoundServiceException extends LogiwebServiceException{

    public UserNotFoundServiceException() {
        super();
    }

    public UserNotFoundServiceException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserNotFoundServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundServiceException(String message) {
        super(message);
    }

    public UserNotFoundServiceException(Throwable cause) {
        super(cause);
    }


}

package com.tsystems.javaschool.logiweb.dao.exceptions;

public class ConstrainOrIntegrityViolationException extends DaoException {

    public ConstrainOrIntegrityViolationException() {
    }

    public ConstrainOrIntegrityViolationException(String message,
            Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ConstrainOrIntegrityViolationException(String message,
            Throwable cause) {
        super(message, cause);
    }

    public ConstrainOrIntegrityViolationException(String message) {
        super(message);
    }

    public ConstrainOrIntegrityViolationException(Throwable cause) {
        super(cause);
    }

}

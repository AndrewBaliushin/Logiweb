package com.tsystems.javaschool.logiweb.dao.exceptions;

public class UserNotFoundDaoException extends DaoException {

    public UserNotFoundDaoException() {
        super();
    }

    public UserNotFoundDaoException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserNotFoundDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundDaoException(String message) {
        super(message);
    }

    public UserNotFoundDaoException(Throwable cause) {
        super(cause);
    }

}

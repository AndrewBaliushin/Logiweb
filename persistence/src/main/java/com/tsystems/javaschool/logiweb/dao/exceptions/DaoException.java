package com.tsystems.javaschool.logiweb.dao.exceptions;

/**
 * Exception for Dao. 
 * 
 * @author Andrey Baliushin
 */
public class DaoException extends Exception {

    public DaoException() {
        super();
    }

    public DaoException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

}

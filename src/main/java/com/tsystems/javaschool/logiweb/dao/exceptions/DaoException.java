package com.tsystems.javaschool.logiweb.dao.exceptions;

/**
 * Exception for Dao. 
 * 
 * @author Andrey Baliushin
 */
public class DaoException extends Exception {
    
    private DaoExceptionCode exceptionCode;
    
    public DaoException(DaoExceptionCode code, String message) {
	super(message);
	exceptionCode = code;
    }
    
    public DaoException(DaoExceptionCode code) {
        exceptionCode = code;
    }
    
    public DaoExceptionCode getExceptionCode() {
        return exceptionCode;
    }

}

package com.tsystems.javaschool.logiweb.dao.exceptions;

/**
 * Enum with Dao exception codes.
 * 
 * @author Andrey Baliushin
 */
public enum DaoExceptionCode {
    
    FAILED_TO_INSERT(1),
    UPDATE_FAILED(2),
    SQL_ERROR(3);
    
    private int exceptionCode;
    
    private DaoExceptionCode(int eCode) {
	this.exceptionCode = eCode;
    }
    
    public int getExceptionCode() {
	return exceptionCode;
    }

}

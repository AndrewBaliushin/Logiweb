package com.tsystems.javaschool.logiweb.service.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Throw if requested record not exist in data storage.
 * 
 * @author Andrey Baliushin
 */ 
public class RecordNotFoundServiceException extends RuntimeException {

    public RecordNotFoundServiceException() {
        super();
    }
    
    public RecordNotFoundServiceException(String msg) {
        super(msg);
    }
}

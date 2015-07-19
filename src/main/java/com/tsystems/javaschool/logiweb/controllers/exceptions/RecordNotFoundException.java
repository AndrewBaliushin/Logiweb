package com.tsystems.javaschool.logiweb.controllers.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Throw if requested record not exist in data storage.
 * 
 * @author Andrey Baliushin
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Record not found") 
public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException() {
        super();
    }
    
    public RecordNotFoundException(String msg) {
        super(msg);
    }
}

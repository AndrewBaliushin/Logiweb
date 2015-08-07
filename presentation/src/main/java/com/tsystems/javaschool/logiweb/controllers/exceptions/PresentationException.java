package com.tsystems.javaschool.logiweb.controllers.exceptions;

/**
 * Exception for presentation layer: controllers and views.
 * 
 * @author Andrey Baliushin
 */
public class PresentationException extends Exception {

    public PresentationException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public PresentationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PresentationException(String message) {
        super(message);
    }

    public PresentationException(Throwable cause) {
        super(cause);
    }

    public PresentationException() {
        
    }

}

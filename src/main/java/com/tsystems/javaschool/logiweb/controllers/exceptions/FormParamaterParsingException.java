package com.tsystems.javaschool.logiweb.controllers.exceptions;

/**
 * Exception represents error in parsing some form parameters. 
 * For example: if 'cargoSize' parameter from Form can't be parsed 
 * to float or 'crewSize' can't be parsed to int.
 * 
 * @author Andrey Baliushin
 */
public class FormParamaterParsingException extends PresentationException {

    public FormParamaterParsingException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FormParamaterParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormParamaterParsingException(String message) {
        super(message);
    }

    public FormParamaterParsingException(Throwable cause) {
        super(cause);
    }

    public FormParamaterParsingException() {
    }
    
    

}

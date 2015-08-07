package com.tsystems.javaschool.logiweb.service.exceptions;

/**
 * Thrown in situations when some checks were performed and failed.
 * 
 * Example: throw when deletion of Truck is forbidden if it has attached orders.
 * Or creation of Driver is forbidden if employee ID is not unique.
 * 
 * @author Andrey Baliushin
 *
 */
public class ServiceValidationException extends LogiwebServiceException {

    public ServiceValidationException() {
    }

    public ServiceValidationException(String message) {
        super(message);
    }

    public ServiceValidationException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceValidationException(Throwable cause) {
        super(cause);
    }

}

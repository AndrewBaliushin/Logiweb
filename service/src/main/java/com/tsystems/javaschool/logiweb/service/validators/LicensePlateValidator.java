package com.tsystems.javaschool.logiweb.service.validators;

public interface LicensePlateValidator {
    
    /**
     * Test string with plate number against some rules.
     * 
     * @param plate
     * @return
     */
    boolean validateLicensePlate(String plate);
}

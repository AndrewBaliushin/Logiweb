package com.tsystems.javaschool.logiweb.service.validators;

import org.springframework.stereotype.Component;

/**
 * Implementation of license plate validation.
 * 
 * @author Andrey Baliushin
 */
@Component
public class LicensePlateValidatorImpl implements LicensePlateValidator {

    /**
     * String pass test if it has 7 chars and
     * contains 5 digits and 2 letters in any order.
     * Russian chars included.
     * 
     * @param plate
     * @return
     */
    @Override
    public boolean validateLicensePlate(String plate) {
        if(!plate.matches("(?ui)^[А-ЯA-Z0-9]{7}$")) { //only letters and numbers (7 times)
            return false;
        } else if(!plate.matches("(?ui)^[0-9]*[А-ЯA-Z]{1}[0-9]*[А-ЯA-Z]{1}[0-9]*$")) { //2 letters anywhere
            return false;
        } else {
            return true;
        }
    }

}

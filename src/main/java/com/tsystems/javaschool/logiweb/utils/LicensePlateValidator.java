package com.tsystems.javaschool.logiweb.utils;

/**
 * Validates that Strig can be used as license plate.
 * 
 * @author Andrey Baliushin
 */
public class LicensePlateValidator {

    private LicensePlateValidator() {
    }
    
    /**
     * String pass test if it has 7 chars and
     * contains 5 digits and 2 letters in any order.
     * Russian chars included.
     * 
     * @param plate
     * @return
     */
    public static boolean validateLicensePlate(String plate) {
        if(!plate.matches("(?ui)^[А-ЯA-Z0-9]{7}$")) { //only letters and numbers (7 times)
            return false;
        } else if(!plate.matches("(?ui)^[0-9]*[А-ЯA-Z]{1}[0-9]*[А-ЯA-Z]{1}[0-9]*$")) { //2 letters anywhere
            return false;
        } else {
            return true;
        }
    }

}

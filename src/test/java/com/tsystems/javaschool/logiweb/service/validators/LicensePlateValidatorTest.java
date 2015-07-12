package com.tsystems.javaschool.logiweb.service.validators;

import static org.junit.Assert.*;
import static com.tsystems.javaschool.logiweb.service.validators.LicensePlateValidator.*;

import org.junit.Test;

public class LicensePlateValidatorTest {

    @Test
    public void testValidate() {
        assertTrue(validateLicensePlate("123z5h7"));
        assertTrue(validateLicensePlate("123zg67"));
        
        assertFalse(validateLicensePlate("123fz6"));
        assertFalse(validateLicensePlate("123f567"));
        
        //Unicode
        assertTrue(validateLicensePlate("123бв67"));
        assertTrue(validateLicensePlate("ц23456ю"));
    }

}

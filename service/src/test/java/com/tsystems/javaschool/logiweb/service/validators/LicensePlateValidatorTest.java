package com.tsystems.javaschool.logiweb.service.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LicensePlateValidatorTest {
    
    private LicensePlateValidator validator;
    
    @Before 
    public void setup() {
        validator = new LicensePlateValidatorImpl();
    }

    @Test
    public void testValidateWhenScateredLetters() {
        assertTrue(validator.validateLicensePlate("12z4g67"));
    }
    
    @Test
    public void testValidateWhenGroupedLetters() {
        assertTrue(validator.validateLicensePlate("123zg67"));
    }
    
    @Test
    public void testValidateWhenUnicode() {
        assertTrue(validator.validateLicensePlate("123бв67"));
        assertTrue(validator.validateLicensePlate("ц23456ю"));
    }
    
    @Test
    public void testValidateWhenShorter() {
        assertFalse(validator.validateLicensePlate("123fz6"));
    }
    
    @Test
    public void testValidateWhenLonger() {
        assertFalse(validator.validateLicensePlate("123fz678"));
    }
    
    @Test
    public void testValidateWhenNotEnoughLetters() {
        assertFalse(validator.validateLicensePlate("123f567"));
    }
    
    @Test
    public void testValidateWhenTooManyLetters() {
        assertFalse(validator.validateLicensePlate("123fzt7"));
    }
}

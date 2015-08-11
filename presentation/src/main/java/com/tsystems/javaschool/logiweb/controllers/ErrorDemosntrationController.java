package com.tsystems.javaschool.logiweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

@Controller
public class ErrorDemosntrationController {

    @RequestMapping("/logiwebServiceException")
    public void throwLogiwebServiceException() throws LogiwebServiceException {
        throw new LogiwebServiceException("Test message.");
    }
}

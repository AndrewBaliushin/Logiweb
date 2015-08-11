package com.tsystems.javaschool.logiweb.controllers.advisers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

@ControllerAdvice
public class ExceptionControllerAdvice {
    
    private @Value("${views.error}") String errorViewPath;
    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(LogiwebServiceException.class)
    public ModelAndView exception(Exception e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName(errorViewPath);
        mav.addObject("errorCode", status.value());
        mav.addObject("errorDiscription", status.getReasonPhrase());
        mav.addObject("errorMsg", e.getMessage());
        return mav;        
    }
}

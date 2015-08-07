package com.tsystems.javaschool.logiweb.controllers;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    
    @RequestMapping(value = "/login")
    public String login() {
        return "auth/Login";
    }
    
}

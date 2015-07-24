package com.tsystems.javaschool.logiweb.controllers;

import java.util.Collection;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tsystems.javaschool.logiweb.entities.status.UserRole;

@Controller
public class FrontPageController {

    @RequestMapping("/")
    public String commonFrontPage() {
        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder
                .getContext().getAuthentication().getAuthorities();

        GrantedAuthority manager = new SimpleGrantedAuthority(
                UserRole.ROLE_MANAGER.name());
        GrantedAuthority driver = new SimpleGrantedAuthority(
                UserRole.ROLE_DRIVER.name());

        if (authorities.contains(manager)) {
            return "forward:/manager";
        } else if (authorities.contains(driver)) {
            return "forward:/driver";
        } 
        
        throw new AccessDeniedException("User role is unknown.");
    }
}

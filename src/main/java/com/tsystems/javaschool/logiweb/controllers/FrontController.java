package com.tsystems.javaschool.logiweb.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tsystems.javaschool.logiweb.LogiwebAppContext;
import com.tsystems.javaschool.logiweb.model.status.UserRole;
import com.tsystems.javaschool.logiweb.service.UserService;

@Controller
@RequestMapping(value = {"/", ""})
public class FrontController {

    private static final Logger LOG = Logger.getLogger(AuthController.class);

    LogiwebAppContext ctx = LogiwebAppContext.getInstance();

    UserService userService = ctx.getUserService();

    @RequestMapping
    public String processLoginForm(HttpServletRequest request,
            HttpServletResponse response) {
        
        UserRole userRole= (UserRole) request.getSession().getAttribute(ctx.SESSION_ATTR_TO_STORE_ROLE);
        
        if (userRole == UserRole.MANAGER) {
            return "forward:/manager";
        } else if (userRole == UserRole.DRIVER){
            return "forward:/driver"; //TODO make this controller
        } else {
            return "forward:/500"; //TODO role not found page
        }
    }

}

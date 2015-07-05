package com.tsystems.javaschool.logiweb.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tsystems.javaschool.logiweb.LogiwebAppContext;
import com.tsystems.javaschool.logiweb.model.User;
import com.tsystems.javaschool.logiweb.model.status.UserRole;
import com.tsystems.javaschool.logiweb.service.UserService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.UserNotFoundServiceException;
import com.tsystems.javaschool.logiweb.utils.AuthUtils;

@Controller
public class AuthController {

    private static final Logger LOG = Logger.getLogger(AuthController.class);
    
    LogiwebAppContext ctx = LogiwebAppContext.INSTANCE;
    
    UserService userService = ctx.getUserService();

    @RequestMapping(value = "/login")
    public ModelAndView processLoginForm(HttpServletRequest request,
            HttpServletResponse response) {
        
        if (AuthUtils.isLoggedIn(request)) {
            redirectToFrontPage(request, response);
        }
        
        String mail = request.getParameter("mail");
        String pass = request.getParameter("pass");
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("auth/Login");
        
        mav.addObject("mail", mail); //insert mail back on login page
        
        if(mail == null) {      //user didn't submit the form
            return mav; 
        }
        
        try {
            if(mail != null) {
                User user = userService.getUserByMd5PassAndMail(mail, pass);
                AuthUtils.startAuthSessionForRole(request.getSession(), user.getRole());
                LOG.info("User id:" + user.getId() + " mail:" + user.getMail() + " is logged in.");
                redirectToFrontPage(request, response);
            }
        } catch (UserNotFoundServiceException e) {
            mav.addObject("error", "User with this pass and mail is not found.");
        } catch (LogiwebServiceException e) {
            LOG.warn("Problems in user Service", e);
            mav.addObject("error", "Error on server");
        }
        
        return mav;
    }
    
    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        AuthUtils.destroyAuthSession(request.getSession());
        try {
            response.sendRedirect(request.getContextPath());
        } catch (IOException e) {
            LOG.warn("IO exception", e);
        }
    }
    
    private void redirectToFrontPage(HttpServletRequest request,
    HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath());
        } catch (IOException e) {
            LOG.warn("IO exception", e);
        }
    }

}

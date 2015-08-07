package com.tsystems.javaschool.logiweb.controllers;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    
    private final static Logger LOG = Logger.getLogger(ManagerController.class);

    @RequestMapping(value = {"", "/"})
    public ModelAndView frontPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/FrontPage");
        return mav;
    }
    
}

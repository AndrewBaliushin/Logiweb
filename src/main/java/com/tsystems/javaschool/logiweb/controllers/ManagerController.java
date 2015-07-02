package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashSet;
import java.util.Set;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tsystems.javaschool.logiweb.LogiwebAppContext;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    
    private LogiwebAppContext ctx = LogiwebAppContext.INSTANCE;
    
    private DriverService driverService = ctx.getDriverService();
    private TrucksService truckService = ctx.getTruckService();
    
    Logger logger = Logger.getLogger(ManagerController.class);
    
    @RequestMapping(value = {"", "/"})
    public ModelAndView frontPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/ManagerFrontPage");
        return mav;
    }
    
    @RequestMapping("/driverList")
    public ModelAndView showDrivers() {  
	ModelAndView mav = new ModelAndView();
	mav.setViewName("manager/ShowDriverList");
	mav.addObject("title", "Drivers List");
	
	Set<Driver> drivers;
        try {
            drivers = driverService.findAllDrivers();
        } catch (LogiwebServiceException e) {
            drivers = new HashSet<Driver>(0);
            logger.warn(e);
        }
	
	mav.addObject("drivers", drivers);
	
	return mav;
    }
    
    @RequestMapping(value = {"/showTrucks"})
    public ModelAndView showTrucks() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/ShowTrucksList");
        
        Set<Truck> trucks;
        try {
            trucks = truckService.findAllTrucks();
        } catch (DaoException e) {
            e.printStackTrace();
            trucks = new HashSet<Truck>(0);
        }
        mav.addObject("trucks", trucks);
        
        return mav;
    }
   
}

package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tsystems.javaschool.logiweb.dao.DaoFactory;
import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.jpa.DaoFactoryJpa;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.service.DriversService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.impl.DriverServiceImpl;
import com.tsystems.javaschool.logiweb.utils.LogiwebJpaHelper;

@Controller
@RequestMapping("/manager")
public class ManagerController {
    
    Logger logger = Logger.getLogger(ManagerController.class);

    //TODO Autowire this block
    private EntityManagerFactory emf = LogiwebJpaHelper
            .getEntityManagerFactory();

    private EntityManager entityManager = emf.createEntityManager();
    
    private DaoFactory daoFactory = new DaoFactoryJpa(entityManager);
    
    DriverDao driverDao = daoFactory.getDriverDao();
    TruckDao truckDao = daoFactory.getTruckDao();
    
    DriversService driverService = new DriverServiceImpl(daoFactory, entityManager);
    //end of block
    
    @RequestMapping(value = {"", "/"})
    public ModelAndView frontPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/ManagerFrontPage");
        mav.addObject("title", "Manager View");
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
            // TODO Figure out what to do
            e.printStackTrace();
        }
	
	mav.addObject("drivers", drivers);
	
	return mav;
    }
   
}

package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.jpa.DriverDaoJpa;
import com.tsystems.javaschool.logiweb.dao.jpa.TruckDaoJpa;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.service.DriversService;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.impl.DriverServiceImpl;
import com.tsystems.javaschool.logiweb.service.impl.TrucksSeviceimpl;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    //TODO Autowire this block
    //TODO remove emf creation to singleton
    private static EntityManagerFactory emf = Persistence
	    .createEntityManagerFactory("logiweb");

    private static EntityManager entityManager = emf.createEntityManager();
    
    DriverDao driverDao = new DriverDaoJpa(Driver.class, entityManager);
    TruckDao truckDao = new TruckDaoJpa(Truck.class, entityManager);
    
    DriversService driverService = new DriverServiceImpl(driverDao, entityManager);
    TrucksService trucksService = new TrucksSeviceimpl(truckDao, entityManager);
    //end of block

    public static final String DRIVER_LIST_URL = "driverList";
    public static final String TRUCK_LIST_URL = "truckList";
    
    @RequestMapping(value = {"", "/"})
    public ModelAndView frontPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/ManagerFrontPage");
        mav.addObject("title", "Manager View");
        mav.addObject("showDriversUrl", "/manager/" + DRIVER_LIST_URL);
        mav.addObject("showTrucksUrl", "/manager/" + TRUCK_LIST_URL);
        return mav;
    }
    
    @RequestMapping(DRIVER_LIST_URL)
    public ModelAndView showDrivers() {
	ModelAndView mav = new ModelAndView();
	mav.setViewName("manager/ShowDriverList");
	mav.addObject("title", "Drivers List");
	
	Set<Driver> drivers;
	try {
	    drivers = driverService.findAllDrivers();
        } catch (DaoException e) {
            e.printStackTrace(); //TODO change to logging
	    drivers = new HashSet<Driver>(0);
        }
	
	mav.addObject("drivers", drivers);
	
	return mav;
    }
    
    @RequestMapping(TRUCK_LIST_URL)
    public ModelAndView showTrucks() {
	ModelAndView mav = new ModelAndView();
	mav.setViewName("manager/ShowTrucksList");
	mav.addObject("title", "Trucks List");
	
	Set<Truck> trucks;
	try {
	    trucks = trucksService.findAllTrucks();
        } catch (DaoException e) {
            e.printStackTrace();
            trucks = new HashSet<Truck>(0);
        }
	
	mav.addObject("trucks", trucks);
	
	return mav;
    }
}

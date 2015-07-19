package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tsystems.javaschool.logiweb.controllers.exceptions.FormParamaterParsingException;
import com.tsystems.javaschool.logiweb.controllers.exceptions.RecordNotFoundException;
import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.status.DriverStatus;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.RouteService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;

@Controller
public class DriverController {
    
    private final static Logger LOG = Logger.getLogger(DriverController.class);

    @Autowired
    private DriverService driverService;
    @Autowired
    private CityService cityService;
    @Autowired
    private RouteService routeService;

    @RequestMapping("manager/showDrivers")
    public ModelAndView showDrivers() {  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/DriverList");
        
        try {
            Set<Driver> drivers = driverService.findAllDrivers();
            mav.addObject("drivers", drivers);
            
            Map<Driver, Float> workingHoursForDrivers = new HashMap<Driver, Float>();
            for (Driver driver : drivers) {
                workingHoursForDrivers.put(driver, driverService.calculateWorkingHoursForDriver(driver));
            }
            mav.addObject("workingHoursForDrivers", workingHoursForDrivers);
        } catch (LogiwebServiceException e) {
            LOG.warn(e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        return mav;
    }
    
    //TODO combine with 'manager/showDrivers'
    @RequestMapping("driver")
    public ModelAndView showDriversForDriver() {  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("driver/DriverList");
        
        try {
            Set<Driver> drivers = driverService.findAllDrivers();
            mav.addObject("drivers", drivers);
            
            Map<Driver, Float> workingHoursForDrivers = new HashMap<Driver, Float>();
            for (Driver driver : drivers) {
                workingHoursForDrivers.put(driver, driverService.calculateWorkingHoursForDriver(driver));
            }
            mav.addObject("workingHoursForDrivers", workingHoursForDrivers);
        } catch (LogiwebServiceException e) {
            LOG.warn(e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        return mav;
    }
    
    @RequestMapping(value = "driver/showDriver", method = RequestMethod.GET)
    public ModelAndView showSingleDriverForDriver(HttpServletRequest request) {  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("driver/SingleDriver");
        
        try {
            int driverId = Integer.parseInt(request.getParameter("driverId"));
            Driver driver = driverService.findDriverById(driverId);
            mav.addObject("driver", driver);
            mav.addObject("workingHours",
                    driverService.calculateWorkingHoursForDriver(driver));
            
            if (driver.getCurrentTruck() != null
                    && driver.getCurrentTruck().getAssignedDeliveryOrder() != null) {
                RouteInformation routeInfo = routeService.getRouteInformationForOrder(driver
                        .getCurrentTruck().getAssignedDeliveryOrder());
                mav.addObject("routeInfo", routeInfo);
            }
            
            mav.addObject("journals", driverService.findDriverJournalsForThisMonth(driver));
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The 'orderId' parameter must not be null, empty or anything other than integer");
        } catch (LogiwebServiceException e) {
            LOG.warn(e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        return mav;
    }
    
    @RequestMapping(value = "/driver/{driverId}")
    public ModelAndView showSingleDriver(@PathVariable("driverId") int driverId) {  
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/SingleDriver");
        
        try {
            Driver driver = driverService.findDriverById(driverId);
            if (driver == null) {
                throw new RecordNotFoundException();
            }
            
            mav.addObject("driver", driver);
            mav.addObject("workingHours",
                    driverService.calculateWorkingHoursForDriver(driver));
            
            if (driver.getCurrentTruck() != null
                    && driver.getCurrentTruck().getAssignedDeliveryOrder() != null) {
                RouteInformation routeInfo = routeService
                        .getRouteInformationForOrder(driver.getCurrentTruck()
                                .getAssignedDeliveryOrder());
                mav.addObject("routeInfo", routeInfo);
            }

            mav.addObject("journals",
                    driverService.findDriverJournalsForThisMonth(driver));
        } catch (LogiwebServiceException e) {
            LOG.warn(e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        return mav;
    }
    
    @RequestMapping(value = {"manager/addDriver"})
    public ModelAndView addDriver (HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        
        if("POST".equals(request.getMethod())) { //form is submitted
            try {
                Driver newDriver = createDriverEntityFromFormParams(request);
                driverService.addDriver(newDriver);
                return new ModelAndView("redirect:/manager/showDrivers");
            } catch (FormParamaterParsingException e) {
                mav.addObject("error", e.getMessage());
            } catch (ServiceValidationException e) {
                mav.addObject("error", e.getMessage());
            } catch (LogiwebServiceException e) {
                mav.addObject("error", "Server error. Check logs.");
            }
        }

        mav.setViewName("manager/AddDriver");
        
        try {
            mav.addObject("cities", cityService.findAllCities());
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        mav.addObject("statuses", DriverStatus.values());
        
        sendBackInputParametersToView(mav, request);
        
        return mav;
    }
    
    /**
     * Removes driver by its ID received in 'driverID' parameter. 
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "manager/deleteDriver", method = RequestMethod.POST)
    @ResponseBody
    public String deleteDriver(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Map<String, String> jsonMap = new HashMap<String, String>();
        
        try {
            int idInt = Integer.parseInt(request.getParameter("driverId"));
            Driver driverToRemove = new Driver();
            driverToRemove.setId(idInt);
            driverService.removeDriver(driverToRemove);
            
            jsonMap.put("msg", "Driver deleted");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonMap.put("msg", "Can't parse Driver id:" + request.getParameter("driverId") + " to integer.");
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonMap.put("msg", e.getMessage());
        } catch (LogiwebServiceException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.warn("Unexpected exception.", e);
            jsonMap.put("msg", "Unexcpected server error. Check logs.");
        }
        
        return gson.toJson(jsonMap);
    }
    
    private Driver createDriverEntityFromFormParams(HttpServletRequest request) throws FormParamaterParsingException {
        Integer employeeId;
        DriverStatus status;
        Integer cityId;
        
        try {
            employeeId = Integer.parseInt(request.getParameter("employeeId"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("Employee ID field is in wrong format. Use integers.");
        }
        
        try {
            cityId = Integer.parseInt(request.getParameter("city"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("City selector must return city ID as integer.");
        }
             
        Driver driver = new Driver();

        City city = new City();
        city.setId(cityId);

        driver.setEmployeeId(employeeId);
        driver.setName(request.getParameter("name"));
        driver.setSurname(request.getParameter("surname"));
        driver.setCurrentCity(city);

        return driver;
    }
    
    /**
     * Send back params received from form.
     * Use this to repopulate form input fields.
     * 
     * @param mav
     * @param request
     * @return
     */
    private ModelAndView sendBackInputParametersToView(ModelAndView mav, HttpServletRequest request) {
        mav.addObject("city", request.getParameter("city"));
        mav.addObject("status", request.getParameter("status"));
        mav.addObject("employeeId", request.getParameter("employeeId"));
        mav.addObject("name", request.getParameter("name"));
        mav.addObject("surname", request.getParameter("surname"));
        
        return mav;
    }

    /**
     * Removes driver by its ID received in 'driverID' parameter. 
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "manager/addDriverToTruck", method = RequestMethod.POST)
    @ResponseBody
    public String addDriverToTruck(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Map<String, String> jsonMap = new HashMap<String, String>();
        
        String[] driverIdsStrings = request.getParameterValues("driversIds");
        if (driverIdsStrings == null || driverIdsStrings.length == 0) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonMap.put("msg", "Drivers are not specified.");
            return gson.toJson(jsonMap);
        }
        
        int[] driversIds = new int[driverIdsStrings.length]; 
        try {
            for (int i = 0; i < driverIdsStrings.length; i++) {
                driversIds[i] = Integer.parseInt(driverIdsStrings[i]);
            }
            
            int truckId = Integer.parseInt(request.getParameter("truckId"));
           
            for (int driverId : driversIds) {
                driverService.assignDriverToTruck(driverId, truckId);
            }
            
            jsonMap.put("msg", "Drivers added to truck");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonMap.put("msg", "Can't parse Driver id:" + request.getParameter("driverId") + " to integer.");
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonMap.put("msg", e.getMessage());
        } catch (LogiwebServiceException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.warn("Unexpected exception.", e);
            jsonMap.put("msg", "Unexcpected server error. Check logs.");
        }
        
        return gson.toJson(jsonMap);
    }
}

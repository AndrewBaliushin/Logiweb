package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tsystems.javaschool.logiweb.controllers.exceptions.RecordNotFoundException;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.status.DriverStatus;
import com.tsystems.javaschool.logiweb.model.DriverModel;
import com.tsystems.javaschool.logiweb.model.ext.ModelToEntityConverter;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.RouteService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;

@Controller
public class DriverController {
    
    private final static Logger LOG = Logger.getLogger(DriverController.class);
    
    private @Value("${views.addOrEditDriver}") String addOrUpdateDriverViewPath;
    private @Value("${views.driverList}") String driverListViewPath;
    private @Value("${views.driverInfo}") String driverInfoViewPath;
    
    private @Value("${bussines.defaultDriverPass}") String defaultDriverPass;
    private @Value("${bussines.driverAccountTemplate}") String driverAccountTemplate;

    @Autowired
    private DriverService driverService;
    @Autowired
    private CityService cityService;
    @Autowired
    private RouteService routeService;

    @RequestMapping("driver")
    public ModelAndView showDrivers() {  
        ModelAndView mav = new ModelAndView();
        mav.setViewName(driverListViewPath);
        
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
    
    @RequestMapping(value = "/driver/{driverId}")
    public ModelAndView showSingleDriver(@PathVariable("driverId") int driverId) {  
        ModelAndView mav = new ModelAndView();
        mav.setViewName(driverInfoViewPath);
        
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
    
    @RequestMapping(value = {"driver/new"}, method = RequestMethod.GET)
    public String showFormForNewDriver (Model model) {
        model.addAttribute("formAction", "new");
        model.addAttribute("driverModel", new DriverModel());
        addCitiesToModel(model);
        return addOrUpdateDriverViewPath;
    }
    
    @RequestMapping(value = {"driver/new"}, method = RequestMethod.POST)
    public String addDriverAndDriverAccount(
            @ModelAttribute("driverModel") @Valid DriverModel driverModel,
            BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("driverModel", driverModel);
            addCitiesToModel(model);
            model.addAttribute("formAction", "new");
            return addOrUpdateDriverViewPath;
        }
        
        try {
            int newDriverId = driverService.addDriverWithAccount(driverModel,
                    convertDriverEmpIdToAccountNameByTemplate(driverModel
                            .getEmployeeId()), defaultDriverPass);
            return "redirect:/driver/" + newDriverId;
        } catch (ServiceValidationException e) {
            model.addAttribute("error", e.getMessage());
            addCitiesToModel(model);
            model.addAttribute("formAction", "new");
            return addOrUpdateDriverViewPath;
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexcpected error happened.");
            throw new RuntimeException(e);
        }        
    }
    
    private String convertDriverEmpIdToAccountNameByTemplate(int driverEmployeeId) {
        String idAsString = String.valueOf(driverEmployeeId);
        String result = driverAccountTemplate.replace("{}", idAsString);
        return result;
    }
    
    @RequestMapping(value = {"driver/{driverId}/edit"}, method = RequestMethod.GET)
    public String showFormForEditDriver (@PathVariable("driverId") int driverId, Model model) {
        model.addAttribute("formAction", "edit");
        
        try {
            Driver driver = driverService.findDriverById(driverId);
            if(driver == null) {
                throw new RecordNotFoundException();
            }
            model.addAttribute("driverModel", ModelToEntityConverter.convertToModel(driver));
            addCitiesToModel(model);
            model.addAttribute("driverStatuses", DriverStatus.values());
            return addOrUpdateDriverViewPath;
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexcpected error happened.");
            throw new RuntimeException(e);
        }
    }
    
    @RequestMapping(value = {"driver/{driverId}/edit"}, method = RequestMethod.POST)
    public String editDriver(@PathVariable("driverId") int driverId,
            @ModelAttribute("driverModel") @Valid DriverModel driverModel,
            BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("driverModel", driverModel);
            addCitiesToModel(model);
            model.addAttribute("formAction", "edit");
            return addOrUpdateDriverViewPath;
        }
        
        try {
            driverService.editDriverAndAccountName(driverModel,
                    convertDriverEmpIdToAccountNameByTemplate(driverModel
                            .getEmployeeId()));
            return "redirect:/driver/" + driverModel.getId();
        } catch (ServiceValidationException e) {
            model.addAttribute("error", e.getMessage());
            addCitiesToModel(model);
            model.addAttribute("formAction", "edit");
            return addOrUpdateDriverViewPath;
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexcpected error happened.");
            throw new RuntimeException(e);
        }        
    }

    private Model addCitiesToModel(Model model) {
        try {
            model.addAttribute("cities", cityService.findAllCities());
            return model;
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
    }
    
    /**
     * Removes driver by its ID received in 'driverID' parameter. 
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "driver/{driverId}/remove", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String deleteDriver(@PathVariable("driverId") int driverId, HttpServletResponse response) {
        try {
            Driver driverToRemove = new Driver();
            driverToRemove.setId(driverId);
            driverService.removeDriverAndAccount(driverToRemove);
            
            return "Driver deleted";
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        } catch (LogiwebServiceException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.warn("Unexpected exception.", e);
            return "Unexcpected server error. Check logs.";
        }
    }

    /**
     * Removes driver by its ID received in 'driverID' parameter. 
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "order/{orderId}/edit/addDriverToTruck", method = RequestMethod.POST)
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

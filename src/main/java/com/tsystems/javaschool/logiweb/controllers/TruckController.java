package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tsystems.javaschool.logiweb.controllers.exceptions.FormParamaterParsingException;
import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.TruckStatus;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

@Controller
public class TruckController {
    
    private final static Logger LOG = Logger.getLogger(TruckController.class);

    @Autowired
    private TrucksService truckService ;
    @Autowired
    private CityService cityService;

    @RequestMapping(value = {"truck"})
    public ModelAndView showTrucks() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/TrucksList");
        
        Set<Truck> trucks;
        try {
            trucks = truckService.findAllTrucks();
        } catch (LogiwebServiceException e) {
            e.printStackTrace();
            trucks = new HashSet<Truck>(0);
        }
        mav.addObject("trucks", trucks);
        
        return mav;
    }
    
    @RequestMapping(value = {"truck/new"})
    public ModelAndView addTruck(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        
        if("POST".equals(request.getMethod())) { //form is submitted
            try {
                Truck newTruck = createTruckEntityFromFormParams(request);
                truckService.addTruck(newTruck);
                return new ModelAndView("redirect:/manager/showTrucks");
            } catch (FormParamaterParsingException e) {
                mav.addObject("error", e.getMessage());
            } catch (ServiceValidationException e) {
                mav.addObject("error", e.getMessage());
            } catch (LogiwebServiceException e) {
                mav.addObject("error", "Server error. Check logs.");
                LOG.warn("Unexpected exception.", e);
            }
        }

        mav.setViewName("manager/AddTruck");
        
        try {
            mav.addObject("cities", cityService.findAllCities());
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        mav.addObject("statuses", TruckStatus.values());
        
        sendBackInputParametersToView(mav, request);
        
        return mav;
    }
    
    /**
     * Removes truck by its ID received in 'truckId' parameter. 
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "manager/deleteTruck", method = RequestMethod.POST)
    @ResponseBody
    public String deleteTruck(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("truckId");
        Gson gson = new Gson();
        Map<String, String> jsonMap = new HashMap<String, String>();
        
        try {
            int idInt = Integer.parseInt(id);
            Truck truckToRemove = new Truck();
            truckToRemove.setId(idInt);
            truckService.removeTruck(truckToRemove);
            
            jsonMap.put("msg", "Truck deleted");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonMap.put("msg", "Can't parse Truck id:" + id + " to integer.");
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

    private Truck createTruckEntityFromFormParams(HttpServletRequest request) throws FormParamaterParsingException {
        Integer crewSize;
        Float cargoCapacity;
        Integer cityId;
        TruckStatus status;
        
        try {
            crewSize = Integer.parseInt(request.getParameter("crew-size"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("Crew size field is in wrong format.");
        }
        
        try {
            cargoCapacity = Float.parseFloat(request.getParameter("cargo-capacity"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("Cargo capacity field is in wrong format. Must be like: '1.5'");
        }
        
        try {
            cityId = Integer.parseInt(request.getParameter("city"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("City selector must return city ID as integer.");
        }
             
        Truck truck = new Truck();
    
        City city = new City();
        city.setId(cityId);
    
        truck.setLicencePlate(request.getParameter("license-plate"));
        truck.setCrewSize(crewSize);
        truck.setCargoCapacity(cargoCapacity);
        truck.setCurrentCity(city);
    
        return truck;
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
        mav.addObject("crewSize", request.getParameter("crew-size"));
        mav.addObject("cargoCapacity", request.getParameter("cargo-capacity"));
        mav.addObject("city", request.getParameter("city"));
        mav.addObject("status", request.getParameter("status"));
        mav.addObject("licensePlate", request.getParameter("license-plate"));
        
        return mav;
    }

}

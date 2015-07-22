package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.tsystems.javaschool.logiweb.controllers.exceptions.FormParamaterParsingException;
import com.tsystems.javaschool.logiweb.controllers.exceptions.RecordNotFoundException;
import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.DriverStatus;
import com.tsystems.javaschool.logiweb.entities.status.TruckStatus;
import com.tsystems.javaschool.logiweb.model.DriverModel;
import com.tsystems.javaschool.logiweb.model.TruckModel;
import com.tsystems.javaschool.logiweb.model.ext.ModelToEntityConverter;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

@Controller
public class TruckController {
    
    private final static Logger LOG = Logger.getLogger(TruckController.class);
    
    private @Value("${views.addOrEditTruck}") String addOrUpdateTruckViewPath;

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
    
    @RequestMapping(value = {"truck/new"}, method = RequestMethod.GET)
    public String showFormForNewTruck (Model model) {
        model.addAttribute("formAction", "new");
        model.addAttribute("truckModel", new TruckModel());
        addCitiesToModel(model);
        return addOrUpdateTruckViewPath;
    }
    
    @RequestMapping(value = {"truck/new"}, method = RequestMethod.POST)
    public String addDriver(@ModelAttribute("truckModel") @Valid TruckModel newTruckModel,
            BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("truckModel", newTruckModel);
            addCitiesToModel(model);
            model.addAttribute("formAction", "new");
            return addOrUpdateTruckViewPath;
        }
        
        try {
            truckService.addTruck(newTruckModel);
            return "redirect:/truck";
        } catch (ServiceValidationException e) {
            model.addAttribute("error", e.getMessage());
            addCitiesToModel(model);
            model.addAttribute("formAction", "new");
            return addOrUpdateTruckViewPath;
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
    
    @RequestMapping(value = {"truck/{truckId}/edit"}, method = RequestMethod.GET)
    public String showFormForEditDriver (@PathVariable("truckId") int truckId, Model model) {
        model.addAttribute("formAction", "edit");
        
        try {
            Truck truck = truckService.findTruckById(truckId);
            if(truck == null) {
                throw new RecordNotFoundException();
            }
            model.addAttribute("truckModel", ModelToEntityConverter.convertToModel(truck));
            addCitiesToModel(model);
            model.addAttribute("truckStatuses", TruckStatus.values());
            return addOrUpdateTruckViewPath;
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexcpected error happened.");
            throw new RuntimeException(e);
        }
    }
    
    @RequestMapping(value = {"truck/{truckId}/edit"}, method = RequestMethod.POST)
    public String editTruck(@PathVariable("truckId") int truckId,
            @ModelAttribute("truckModel") @Valid TruckModel truckModel,
            BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("driverModel", truckModel);
            addCitiesToModel(model);
            model.addAttribute("formAction", "edit");
            return addOrUpdateTruckViewPath;
        }
        
        try {
            truckService.editTruck(truckModel);
            return "redirect:/truck";
        } catch (ServiceValidationException e) {
            model.addAttribute("error", e.getMessage());
            addCitiesToModel(model);
            model.addAttribute("formAction", "edit");
            return addOrUpdateTruckViewPath;
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexcpected error happened.");
            throw new RuntimeException(e);
        }        
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

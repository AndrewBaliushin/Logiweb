package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashSet;
import java.util.Set;

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

import com.tsystems.javaschool.logiweb.controllers.exceptions.RecordNotFoundException;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.TruckStatus;
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
    private @Value("${views.truckList}") String truckListViewPath;

    @Autowired
    private TrucksService truckService ;
    @Autowired
    private CityService cityService;

    @RequestMapping(value = {"truck"})
    public ModelAndView showTrucks() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(truckListViewPath);
        
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
    public String showFormForNewTruck (Model model) throws LogiwebServiceException {
        model.addAttribute("formAction", "new");
        model.addAttribute("truckModel", new TruckModel());
        addCitiesToModel(model);
        return addOrUpdateTruckViewPath;
    }
    
    @RequestMapping(value = {"truck/new"}, method = RequestMethod.POST)
    public String addTruck(@ModelAttribute("truckModel") @Valid TruckModel newTruckModel,
            BindingResult result, Model model) throws LogiwebServiceException {
        
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
        }   
    }
    
    private Model addCitiesToModel(Model model) throws LogiwebServiceException {
        model.addAttribute("cities", cityService.findAllCities());
        return model;
    }
    
    @RequestMapping(value = {"truck/{truckId}/edit"}, method = RequestMethod.GET)
    public String showFormForEditDriver (@PathVariable("truckId") int truckId, Model model) throws LogiwebServiceException {
        model.addAttribute("formAction", "edit");

        Truck truck = truckService.findTruckById(truckId);
        if (truck == null) {
            throw new RecordNotFoundException();
        }
        model.addAttribute("truckModel",
                ModelToEntityConverter.convertToModel(truck));
        addCitiesToModel(model);
        model.addAttribute("truckStatuses", TruckStatus.values());
        return addOrUpdateTruckViewPath;
    }
    
    @RequestMapping(value = {"truck/{truckId}/edit"}, method = RequestMethod.POST)
    public String editTruck(@PathVariable("truckId") int truckId,
            @ModelAttribute("truckModel") @Valid TruckModel truckModel,
            BindingResult result, Model model) throws LogiwebServiceException {
        
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
        }     
    }
    
    /**
     * Removes truck by its ID received in 'truckId' parameter. 
     * 
     * @param request
     * @return
     * @throws LogiwebServiceException 
     */
    @RequestMapping(value = "truck/{truckId}/remove", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String deleteDriver(@PathVariable("truckId") int truckId, HttpServletResponse response) throws LogiwebServiceException {
        try {
            truckService.removeTruck(truckId);
            return "Driver deleted";
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }

}

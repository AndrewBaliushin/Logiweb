package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tsystems.javaschool.logiweb.controllers.exceptions.RecordNotFoundException;
import com.tsystems.javaschool.logiweb.controllers.ext.CityUtils;
import com.tsystems.javaschool.logiweb.entities.DriverShiftJournal;
import com.tsystems.javaschool.logiweb.entities.status.DriverStatus;
import com.tsystems.javaschool.logiweb.model.DriverModel;
import com.tsystems.javaschool.logiweb.model.DriverUserModel;
import com.tsystems.javaschool.logiweb.model.UserModel;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.service.facades.DriverFacade;
import com.tsystems.javaschool.logiweb.utils.DateUtils;

@Controller
public class DriverController {
    
    @Value("${views.addOrEditDriver}")
    private String addOrUpdateDriverViewPath;
    @Value("${views.driverList}") 
    private String driverListViewPath;
    @Value("${views.driverInfo}")
    private String driverInfoViewPath;
    
    @Value("${bussines.defaultDriverPass}") 
    private String defaultDriverPass;
    @Value("${bussines.driverAccountTemplate}") 
    private String driverAccountTemplate;

    @Autowired
    private DriverService driverService;
    @Autowired
    private DriverFacade driverFacade;
    @Autowired
    private CityUtils cityUtils;

    @RequestMapping("driver")
    public String showDrivers(Model model) throws LogiwebServiceException {  
        Set<DriverModel> drivers = driverService.findAllDrivers();
        model.addAttribute("drivers", drivers);
        
        for (DriverModel driver : drivers) {
            driver.setWorkingHoursThisMonth(driverService
                    .calculateWorkingHoursForDriver(driver.getId()));
        }
        cityUtils.addCitiesToModel(model);
        
        return driverListViewPath;
    }    
    
    @RequestMapping(value = "/driver/{driverId}")
    public String showSingleDriver(@PathVariable("driverId") int driverId,
            Model model) throws LogiwebServiceException {
        authorizeAccesToDriverInfo(driverId);

        DriverModel driver = driverFacade
                .getDriverModelWithFullInfo(driverId);
        
        if (driver == null) {
            throw new RecordNotFoundException();
        }
        
        if (driver.getCoDriversIds() != null) {
            Map<Integer, DriverModel> coDrivers = new HashMap<Integer, DriverModel>();
            for (Integer coDriverId : driver.getCoDriversIds()) {
                coDrivers.put(coDriverId, driverService.findDriverById(coDriverId));
            }
            model.addAttribute("coDrivers", coDrivers);
        }
        
        model.addAttribute("driver", driver);
        cityUtils.addCitiesToModel(model);
        return driverInfoViewPath;
    }
    
    private void authorizeAccesToDriverInfo(int driverId) throws LogiwebServiceException {
        UserModel user = (UserModel) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        //grant full access to everyone except drivers
        if (!(user instanceof DriverUserModel)) {
            return;
        }
        
        int loggedinDriverId = ((DriverUserModel) user).getDriverLogiwebId();
        if (driverId == loggedinDriverId) {
            return;
        }
        
        DriverModel driver = driverService.findDriverById(loggedinDriverId);
        if (driver.getCoDriversIds() != null) {
            Set<Integer> coDriversIds = driver.getCoDriversIds();
            if (coDriversIds.contains(driverId)) {
                return;
            }
        }
    
        throw new AccessDeniedException("Only managers and co-drivers have access to info.");
    }

    @RequestMapping(value = {"driver/new"}, method = RequestMethod.GET)
    public String showFormForNewDriver (Model model) throws LogiwebServiceException {
        model.addAttribute("formAction", "new");
        model.addAttribute("driverModel", new DriverModel());
        cityUtils.addCitiesToModel(model);
        return addOrUpdateDriverViewPath;
    }
    
    @RequestMapping(value = {"driver/new"}, method = RequestMethod.POST)
    public String addDriverAndDriverAccount(
            @ModelAttribute("driverModel") @Valid DriverModel driverModel,
            BindingResult result, Model model) throws LogiwebServiceException {
        
        if (result.hasErrors()) {
            model.addAttribute("driverModel", driverModel);
            cityUtils.addCitiesToModel(model);
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
            cityUtils.addCitiesToModel(model);
            model.addAttribute("formAction", "new");
            return addOrUpdateDriverViewPath;
        }     
    }
    
    private String convertDriverEmpIdToAccountNameByTemplate(int driverEmployeeId) {
        String idAsString = String.valueOf(driverEmployeeId);
        return driverAccountTemplate.replace("{}", idAsString);
    }
    
    @RequestMapping(value = {"driver/{driverId}/edit"}, method = RequestMethod.GET)
    public String showFormForEditDriver(@PathVariable("driverId") int driverId,
            Model model) throws LogiwebServiceException {
        model.addAttribute("formAction", "edit");

        DriverModel driver = driverService.findDriverById(driverId);
        if (driver == null) {
            throw new RecordNotFoundException();
        }
        model.addAttribute("driverModel", driver);
        cityUtils.addCitiesToModel(model);
        model.addAttribute("driverStatuses", DriverStatus.values());
        return addOrUpdateDriverViewPath;
        
    }
    
    @RequestMapping(value = {"driver/{driverId}/edit"}, method = RequestMethod.POST)
    public String editDriver(
            @ModelAttribute("driverModel") @Valid DriverModel driverModel,
            BindingResult result, Model model) throws LogiwebServiceException {
        
        if (result.hasErrors()) {
            model.addAttribute("driverModel", driverModel);
            cityUtils.addCitiesToModel(model);
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
            cityUtils.addCitiesToModel(model);
            model.addAttribute("formAction", "edit");
            return addOrUpdateDriverViewPath;
        }     
    }
    
    /**
     * Removes driver by its ID received in 'driverID' parameter. 
     * 
     * @param request
     * @return
     * @throws LogiwebServiceException 
     */
    @RequestMapping(value = "driver/{driverId}/remove", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String deleteDriver(@PathVariable("driverId") int driverId,
            HttpServletResponse response) throws LogiwebServiceException {
        try {
            driverService.removeDriverAndAccount(driverId);
            return "Driver deleted";
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }

    /**
     * Removes driver by its ID received in 'driverID' parameter. 
     * 
     * @param request
     * @return
     * @throws LogiwebServiceException 
     */
    @RequestMapping(value = "order/{orderId}/edit/addDriverToTruck", method = RequestMethod.POST)
    @ResponseBody
    public String addDriverToTruck(
            @RequestParam("driversIds") int[] driversIds,
            @RequestParam("truckId") int truckId, HttpServletResponse response)
            throws LogiwebServiceException {
        try {
            for (int driverId : driversIds) {
                driverService.assignDriverToTruck(driverId, truckId);
            }
            return "Drivers are added to truck";
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        } 
    }
    
    /**
     * Produce JSON in specific for cal-heatmap format. Method returns data on
     * driver working hours separated by 1 hour intervals.
     * 
     * Example: {"1420070400" : 1} "1420070400" -- is unix timestamp 1 --
     * intensity in this time point *
     * 
     * @see https://kamisama.github.io/cal-heatmap/
     * 
     * @param driverId
     * @return
     * @throws LogiwebServiceException
     */
    @RequestMapping(value = { "driver/{driverId}/calendarHeatMapData" }, method = RequestMethod.GET)
    @ResponseBody 
    public Map<String, Integer> makeCalendarHeatMapJsonData(
            @PathVariable("driverId") int driverId)
            throws LogiwebServiceException {
        Map<String, Integer> calendarHeatData = new HashMap<String, Integer>();

        DriverModel driver = driverService.findDriverById(driverId);
        if (driver == null) {
            throw new RecordNotFoundException();
        }
        Set<DriverShiftJournal> journals = driverService
                .findDriverJournalsForThisMonth(driver.getId());

        for (DriverShiftJournal j : journals) {
            Map<String, Integer> counter = DateUtils
                    .convertIntervalToCalHeatmapFormat(j.getShiftBeggined(),
                            j.getShiftEnded());
            calendarHeatData.putAll(counter);
        }

        return calendarHeatData;
    }
}

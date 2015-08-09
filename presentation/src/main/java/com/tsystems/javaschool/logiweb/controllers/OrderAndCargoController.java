package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tsystems.javaschool.logiweb.controllers.exceptions.FormParamaterParsingException;
import com.tsystems.javaschool.logiweb.controllers.exceptions.RecordNotFoundException;
import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.service.CargoService;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.OrderService;
import com.tsystems.javaschool.logiweb.service.RouteService;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;

@Controller
public class OrderAndCargoController {
    
    @Value("${bussines.maxWorkingHours}")
    private float driverMonthlyWorkingHoursLimit;
    
    private @Value("${views.orderList}") String orderListViewPath;
    private @Value("${views.editOrder}") String editOrderViewPath;
    private @Value("${views.cargoesList}") String cargoesListViewPath;
    
    private final static Logger LOG = Logger.getLogger(OrderAndCargoController.class);

    private @Autowired CityService cityService;
    private @Autowired OrderService orderService;
    private @Autowired RouteService routeService;
    private @Autowired TrucksService truckService;
    private @Autowired DriverService driverService;
    private @Autowired CargoService cargoService;

    @RequestMapping(value = {"order/{orderId}/edit", "order/{orderId}"}, method = RequestMethod.GET)
    public ModelAndView editOrder(@PathVariable("orderId") int orderId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(editOrderViewPath);
        
        RouteInformation routeInfo = null;
        DeliveryOrder order = null;
        try {
            order = orderService.findOrderById(orderId);
            if (order == null) throw new RecordNotFoundException("Order #" + orderId + " not exist.");

            routeInfo = routeService
                    .getRouteInformationForOrder(order);

            mav.addObject("orderId", orderId);
            mav.addObject("order", order);
            mav.addObject("routeInfo", routeInfo);
            mav.addObject("maxWorkingHoursLimit", driverMonthlyWorkingHoursLimit);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The 'orderId' parameter must not be null, empty or anything other than integer");
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        try {
            mav.addObject("cities", cityService.findAllCities());
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
        }
        
        //suggest trucks
        try {
            if (order.getAssignedTruck() == null) {
                Set<Truck> suggestedTrucks = truckService
                        .findFreeAndUnbrokenByCargoCapacity(routeInfo
                                .getMaxWeightOnCourse());
                mav.addObject("suggestedTrucks", suggestedTrucks);
            }
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
        }
        
        //suggest drivers
        try {
            if (order.getAssignedTruck() != null) {
                float workingHoursLimit = driverMonthlyWorkingHoursLimit - routeInfo.getEstimatedTime();
                
                Set<Driver> suggestedDrivers = driverService
                        .findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(
                                order.getAssignedTruck().getCurrentCity(),
                                workingHoursLimit);
                mav.addObject("suggestedDrivers", suggestedDrivers);
                
                Map<Driver, Float> workingHoursForDrivers = new HashMap<Driver, Float>();
                for (Driver driver : suggestedDrivers) {
                    workingHoursForDrivers.put(driver, driverService.calculateWorkingHoursForDriver(driver.getId()));
                }
                mav.addObject("workingHoursForDrivers", workingHoursForDrivers);
            }
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        mav.addObject("statuses", OrderStatus.values());
        
        return mav;
    }
    
    /**
     * Add cargo to order.
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/{orderId}/edit/addCargo", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String addCargoToOrder(@PathVariable("orderId") int orderId, HttpServletRequest request, HttpServletResponse response) {
        try {
            Cargo newCargo = createDetachedCargoEntityFromRequestParams(request);
            cargoService.addCargo(newCargo);
            return "Cargo added";
        } catch (FormParamaterParsingException  | ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        } catch (LogiwebServiceException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.warn("Unexpected exception.", e);
            return "Unexcpected server error. Check logs.";
        }
    }
    

    /**
     * Assign truck to order.
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/order/{orderId}/edit/assignTruck", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String assignTruckToOrder(@PathVariable("orderId") int orderId, HttpServletRequest request, HttpServletResponse response) {
        int truckId = 0;        
        try {
            truckId = Integer.parseInt(request.getParameter("truckId"));
        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "Order and truck id must be equal or more than 0.";
        }
        
        try {
            orderService.assignTruckToOrder(truckId, orderId);  
            return "Truck assigned";
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
     * Remove truck and drivers from this order.
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "order/{orderId}/edit/removeDriversAndTruck", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String removeDriversAndTruckFromOrder(@PathVariable("orderId") int orderId, HttpServletResponse response) {
       try {
            DeliveryOrder order = orderService.findOrderById(orderId);
            Truck truck = order.getAssignedTruck();
            if(truck != null) {
                truckService.removeAssignedOrderAndDriversFromTruck(truck.getId());  
                return "Drivers and Truck relieved from order.";
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return "Truck is not assigned";
            }
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
     * Assign truck to order.
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "order/{orderId}/edit/setStatusReady", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String setStatusReady(@PathVariable("orderId") int orderId, HttpServletResponse response) {
       try {
            orderService.setReadyStatusForOrder(orderId);  
            return "Status 'READY' is set";
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        } catch (LogiwebServiceException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.warn("Unexpected exception.", e);
            return "Unexcpected server error. Check logs.";
        }
    }
    
    @RequestMapping(value = {"/order/new"})
    public String addOrder() {
        try {
            DeliveryOrder newOrder = new DeliveryOrder();
            newOrder.setStatus(OrderStatus.NOT_READY);
            orderService.addNewOrder(newOrder);
            return "redirect:/order/"
            + newOrder.getId() + "/edit";
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
    }
    
    @RequestMapping(value = {"/order"})
    public ModelAndView showOrders() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(orderListViewPath);
        
        try {
            mav.addObject("orders", orderService.findAllOrders());
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        return mav;
    }
    
    @RequestMapping(value = {"/cargo"})
    public ModelAndView showCargoes() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(cargoesListViewPath);
        
        try {
            mav.addObject("cargoes", cargoService.findAllCargoes());
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
            throw new RuntimeException("Unrecoverable server exception.", e);
        }
        
        return mav;
    }
    
    private Cargo createDetachedCargoEntityFromRequestParams(HttpServletRequest request) throws FormParamaterParsingException {
        int orderId;
        float cargoWeight;
        int originCityId; 
        int destinationCityId;
        
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("Order id ( " + request.getParameter("orderId") + ") is in wrong format or null");
        }
        
        try {
            cargoWeight = Float.parseFloat(request.getParameter("cargoWeight"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("Cargo weight (" + request.getParameter("cargoWeight") + ") is in wrong format or null");
        }
        
        try {
            originCityId = Integer.parseInt(request.getParameter("originCity"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("Origin city (" + request.getParameter("originCity") + ") is in wrong format or null");
        }
        
        try {
            destinationCityId = Integer.parseInt(request.getParameter("destinationCity"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("Destination city(" + request.getParameter("destinationCity") + ") is in wrong format or null");
        }
        
        City originCity = new City();
        City destinationCity = new City();
        originCity.setId(originCityId);
        destinationCity.setId(destinationCityId);
        
        DeliveryOrder orderForThisCargo = new DeliveryOrder();
        orderForThisCargo.setId(orderId);
        
        Cargo newCargo = new Cargo();
        newCargo.setOrderForThisCargo(orderForThisCargo);
        newCargo.setTitle(request.getParameter("cargoTitle"));
        newCargo.setWeight(cargoWeight);
        newCargo.setOriginCity(originCity);
        newCargo.setDestinationCity(destinationCity);
        
        return newCargo;
    }
}

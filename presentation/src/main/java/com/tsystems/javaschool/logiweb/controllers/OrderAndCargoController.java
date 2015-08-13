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
import com.tsystems.javaschool.logiweb.model.TruckModel;
import com.tsystems.javaschool.logiweb.service.CargoService;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.OrderService;
import com.tsystems.javaschool.logiweb.service.RouteService;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;
import com.tsystems.javaschool.logiweb.utils.DateUtils;

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
    public ModelAndView editOrder(@PathVariable("orderId") int orderId) throws LogiwebServiceException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(editOrderViewPath);

        DeliveryOrder order = orderService.findOrderById(orderId);
        if (order == null) {
            throw new RecordNotFoundException("Order #" + orderId
                    + " not exist.");
        }

        RouteInformation routeInfo = routeService.getRouteInformationForOrder(order);

        mav.addObject("orderId", orderId);
        mav.addObject("order", order);
        mav.addObject("routeInfo", routeInfo);
        mav.addObject("maxWorkingHoursLimit", driverMonthlyWorkingHoursLimit);
        
        mav.addObject("cities", cityService.findAllCities());
        
        //suggest trucks
        if (order.getAssignedTruck() == null) {
            Set<TruckModel> suggestedTrucks = truckService
                    .findFreeAndUnbrokenByCargoCapacity(routeInfo
                            .getMaxWeightOnCourse());
            mav.addObject("suggestedTrucks", suggestedTrucks);
        }        
        
        //suggest drivers
        if (order.getAssignedTruck() != null) {
            float workingHoursLimit = calcMaxWorkingHoursThatDriverCanHave(routeInfo
                    .getEstimatedTime());

            Set<Driver> suggestedDrivers = driverService
                    .findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(
                            order.getAssignedTruck().getCurrentCity(),
                            workingHoursLimit);
            mav.addObject("suggestedDrivers", suggestedDrivers);

            Map<Driver, Float> workingHoursForDrivers = new HashMap<Driver, Float>();
            for (Driver driver : suggestedDrivers) {
                workingHoursForDrivers.put(driver, driverService
                        .calculateWorkingHoursForDriver(driver.getId()));
            }
            mav.addObject("workingHoursForDrivers", workingHoursForDrivers);
        }
        
        mav.addObject("statuses", OrderStatus.values());
        
        return mav;
    }
    
    /**
     * Account for possibility that order delivery can take all time in this 
     * month and some in next. 
     * 
     * If there is not enough time in this month to finish order 
     * and if it wont take more hours in next month than allowed by 
     * business rules then we limit hours to what is left in this month.
     * @return limited to this month hours value or original value
     */
    private float calcMaxWorkingHoursThatDriverCanHave(float hoursToDeliver) {
        float diff = DateUtils.getHoursUntilEndOfMonth() - hoursToDeliver;
        if (diff < 0 && driverMonthlyWorkingHoursLimit + diff > 0) {
            return DateUtils.getHoursUntilEndOfMonth();
        } else {
            return driverMonthlyWorkingHoursLimit
                    - hoursToDeliver;
        }
    }
    
    /**
     * Add cargo to order.
     * 
     * @param request
     * @return
     * @throws LogiwebServiceException 
     */
    @RequestMapping(value = "/order/{orderId}/edit/addCargo", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String addCargoToOrder(@PathVariable("orderId") int orderId,
            HttpServletRequest request, HttpServletResponse response)
            throws LogiwebServiceException {
        try {
            Cargo newCargo = createDetachedCargoEntityFromRequestParams(request);
            cargoService.addCargo(newCargo);
            return "Cargo added";
        } catch (FormParamaterParsingException  | ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }
    

    /**
     * Assign truck to order.
     * 
     * @param request
     * @return
     * @throws LogiwebServiceException 
     */
    @RequestMapping(value = "/order/{orderId}/edit/assignTruck", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String assignTruckToOrder(@PathVariable("orderId") int orderId,
            HttpServletRequest request, HttpServletResponse response)
            throws LogiwebServiceException {
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
        }
    }
    
    /**
     * Remove truck and drivers from this order.
     * 
     * @param request
     * @return
     * @throws LogiwebServiceException 
     */
    @RequestMapping(value = "order/{orderId}/edit/removeDriversAndTruck", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String removeDriversAndTruckFromOrder(
            @PathVariable("orderId") int orderId, HttpServletResponse response)
            throws LogiwebServiceException {
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
        }
    }
    
    /**
     * Assign truck to order.
     * 
     * @param request
     * @return
     * @throws LogiwebServiceException 
     */
    @RequestMapping(value = "order/{orderId}/edit/setStatusReady", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String setStatusReady(@PathVariable("orderId") int orderId, HttpServletResponse response) throws LogiwebServiceException {
       try {
            orderService.setReadyStatusForOrder(orderId);  
            return "Status 'READY' is set";
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return e.getMessage();
        }
    }
    
    @RequestMapping(value = {"/order/new"})
    public String addOrder() throws LogiwebServiceException {
        DeliveryOrder newOrder = new DeliveryOrder();
        newOrder.setStatus(OrderStatus.NOT_READY);
        orderService.addNewOrder(newOrder);
        return "redirect:/order/" + newOrder.getId() + "/edit";
    }
    
    @RequestMapping(value = { "/order" })
    public ModelAndView showOrders() throws LogiwebServiceException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(orderListViewPath);
        mav.addObject("orders", orderService.findAllOrders());
        return mav;
    }
    
    @RequestMapping(value = { "/cargo" })
    public ModelAndView showCargoes() throws LogiwebServiceException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(cargoesListViewPath);
        mav.addObject("cargoes", cargoService.findAllCargoes());
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

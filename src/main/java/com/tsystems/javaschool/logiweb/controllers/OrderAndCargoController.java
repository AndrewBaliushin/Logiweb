package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.tsystems.javaschool.logiweb.LogiwebAppContext;
import com.tsystems.javaschool.logiweb.controllers.exceptions.FormParamaterParsingException;
import com.tsystems.javaschool.logiweb.model.Cargo;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.model.status.DriverStatus;
import com.tsystems.javaschool.logiweb.model.status.OrderStatus;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.OrdersAndCargoService;
import com.tsystems.javaschool.logiweb.service.RouteService;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;

@Controller
public class OrderAndCargoController {
    
    private final static Float MAX_WORKING_HOURS_LIMIT = 176f;
    
    private final static Logger LOG = Logger.getLogger(OrderAndCargoController.class);

    private LogiwebAppContext ctx = LogiwebAppContext.getInstance();
    
    private CityService cityService = ctx.getCityService();
    private OrdersAndCargoService orderAndCaroService = ctx.getOrdersAndCargoService();
    private RouteService routeService = ctx.getRouteService();
    private TrucksService truckService = ctx.getTruckService();
    private DriverService driverService = ctx.getDriverService();

    @RequestMapping(value = {"manager/editOrder"}, method = RequestMethod.GET)
    public ModelAndView editOrder(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/EditOrder");
        
        RouteInformation routeInfo = null;
        DeliveryOrder order = null;
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            order = orderAndCaroService.findOrderById(orderId);
            if (order == null) throw new IllegalArgumentException("Order #" + orderId + " not exist.");

            routeInfo = routeService
                    .getRouteInformationForOrder(order);

            mav.addObject("orderId", orderId);
            mav.addObject("order", order);
            mav.addObject("routeInfo", routeInfo);
            mav.addObject("maxWorkingHoursLimit", MAX_WORKING_HOURS_LIMIT);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The 'orderId' parameter must not be null, empty or anything other than integer");
        } catch (LogiwebServiceException e) {
            LOG.error("Unknown problem.", e);
            // TODO Figure out how to throw 500 Server err.
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
                float workingHoursLimit = MAX_WORKING_HOURS_LIMIT - routeInfo.getEstimatedTime();
                
                Set<Driver> suggestedDrivers = driverService
                        .findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(
                                order.getAssignedTruck().getCurrentCity(),
                                workingHoursLimit);
                mav.addObject("suggestedDrivers", suggestedDrivers);
                
                Map<Driver, Float> workingHoursForDrivers = new HashMap<Driver, Float>();
                for (Driver driver : suggestedDrivers) {
                    workingHoursForDrivers.put(driver, driverService.calculateWorkingHoursForDriver(driver));
                }
                mav.addObject("workingHoursForDrivers", workingHoursForDrivers);
            }
        } catch (LogiwebServiceException e) {
            LOG.warn("Unexpected exception.", e);
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
    @RequestMapping(value = "manager/addCargo", method = RequestMethod.POST)
    @ResponseBody
    public String addCargoToOrder(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Map<String, String> jsonResponseMap = new HashMap<String, String>();
        
        try {
            Cargo newCargo = createDetachedCargoEntityFromRequestParams(request);

            orderAndCaroService.addCargo(newCargo);
        } catch (FormParamaterParsingException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponseMap.put("msg", e.getMessage());
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponseMap.put("msg", e.getMessage());
        } catch (LogiwebServiceException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOG.warn("Unexpected exception.", e);
            jsonResponseMap.put("msg", "Unexcpected server error. Check logs.");
        }
        
        return gson.toJson(jsonResponseMap);
    }
    

    /**
     * Assign truck to order.
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "manager/assignTruck", method = RequestMethod.POST)
    @ResponseBody
    public String assignTruckToOrder(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Map<String, String> jsonResponseMap = new HashMap<String, String>();
        
        int orderId = 0;
        int truckId = 0;        
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
            truckId = Integer.parseInt(request.getParameter("truckId"));
        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponseMap.put("msg", "Order and truck id must be equal or more than 0.");
        }
        
        try {
            Truck truck = new Truck();
            truck.setId(truckId);
            orderAndCaroService.assignTruckToOrder(truck, orderId);            
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponseMap.put("msg", e.getMessage());
        } catch (LogiwebServiceException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOG.warn("Unexpected exception.", e);
            jsonResponseMap.put("msg", "Unexcpected server error. Check logs.");
        }
        
        return gson.toJson(jsonResponseMap);
    }
    
    /**
     * Remove truck and drivers from this order.
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "manager/removeDriversAndTruckFromOrder", method = RequestMethod.POST)
    @ResponseBody
    public String removeDriversAndTruckFromOrder(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Map<String, String> jsonResponseMap = new HashMap<String, String>();
        
        int orderId = 0;     
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponseMap.put("msg", "Order ID must be equal or more than 0.");
        }
        
        try {
            DeliveryOrder order = orderAndCaroService.findOrderById(orderId);
            Truck truck = order.getAssignedTruck();
            if(truck != null) {
                truckService.removeAssignedOrderAndDriversFromTruck(truck);  
            }
        } catch (LogiwebServiceException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOG.warn("Unexpected exception.", e);
            jsonResponseMap.put("msg", "Unexcpected server error. Check logs.");
        }
        
        return gson.toJson(jsonResponseMap);
    }
    
    /**
     * Assign truck to order.
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "manager/changeOrderStatus", method = RequestMethod.POST)
    @ResponseBody
    public String changeOrderStatus(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Map<String, String> jsonResponseMap = new HashMap<String, String>();
        
        int orderId = 0;  
        OrderStatus status = null;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
            status = OrderStatus.valueOf(request.getParameter("orderStatus"));
        } catch (IllegalArgumentException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponseMap.put("msg", "Order id or status is in wrong format.");
        }
        
        try {
            DeliveryOrder order = orderAndCaroService.findOrderById(orderId);
            orderAndCaroService.setStatusForOrder(status, order);            
        } catch (ServiceValidationException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponseMap.put("msg", e.getMessage());
        } catch (LogiwebServiceException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            LOG.warn("Unexpected exception.", e);
            jsonResponseMap.put("msg", "Unexcpected server error. Check logs.");
        }
        
        return gson.toJson(jsonResponseMap);
    }
    
    @RequestMapping(value = {"manager/addOrder"})
    public ModelAndView addOrder() {
        DeliveryOrder newOrder = new DeliveryOrder();
        newOrder.setStatus(OrderStatus.NOT_READY);
        try {
            orderAndCaroService.addNewOrder(newOrder);
        } catch (LogiwebServiceException e) {
            LOG.error(e);
            //trigger HTTP Status 500 response
            throw new RuntimeException("Unknown and unexpected error occured while creating new Delivery Order", e);
        }
        
        ModelAndView mav = new ModelAndView("redirect:/manager/editOrder");
        mav.addObject("orderId", newOrder.getId());
        return mav;
    }
    
    @RequestMapping(value = {"manager/showOrders"})
    public ModelAndView showOrders() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/OrderList");
        
        try {
            mav.addObject("orders", orderAndCaroService.findAllOrders());
        } catch (LogiwebServiceException e) {
            //TODO proper exception
            mav.addObject("error", "Server error. Check logs.");
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

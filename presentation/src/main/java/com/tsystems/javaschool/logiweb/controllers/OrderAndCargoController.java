package com.tsystems.javaschool.logiweb.controllers;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tsystems.javaschool.logiweb.controllers.exceptions.FormParamaterParsingException;
import com.tsystems.javaschool.logiweb.controllers.exceptions.RecordNotFoundException;
import com.tsystems.javaschool.logiweb.controllers.ext.CityUtils;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.model.CargoModel;
import com.tsystems.javaschool.logiweb.model.DriverModel;
import com.tsystems.javaschool.logiweb.model.OrderModel;
import com.tsystems.javaschool.logiweb.model.TruckModel;
import com.tsystems.javaschool.logiweb.service.CargoService;
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
    
    @Value("${views.orderList}") 
    private String orderListViewPath;
    @Value("${views.editOrder}") 
    private String editOrderViewPath;
    @Value("${views.cargoesList}") 
    private String cargoesListViewPath;
    
    @Autowired 
    private OrderService orderService;
    @Autowired 
    private RouteService routeService;
    @Autowired 
    private TrucksService truckService;
    @Autowired 
    private DriverService driverService;
    @Autowired 
    private CargoService cargoService;
    @Autowired 
    private CityUtils cityUtils;

    @RequestMapping(value = { "order/{orderId}/edit", "order/{orderId}" }, method = RequestMethod.GET)
    public String editOrder(@PathVariable("orderId") int orderId, Model model)
            throws LogiwebServiceException {
        OrderModel order = orderService.findOrderById(orderId);
        if (order == null) {
            throw new RecordNotFoundException("Order #" + orderId
                    + " not exist.");
        }

        RouteInformation routeInfo = routeService
                .getRouteInformationForOrder(order.getId());

        model.addAttribute("orderId", orderId);
        model.addAttribute("order", order);
        model.addAttribute("routeInfo", routeInfo);
        model.addAttribute("maxWorkingHoursLimit", driverMonthlyWorkingHoursLimit);
        
        //suggest trucks
        if (order.getAssignedTruck() == null) {
            Set<TruckModel> suggestedTrucks = truckService
                    .findFreeAndUnbrokenByCargoCapacity(routeInfo
                            .getMaxWeightOnCourse());
            model.addAttribute("suggestedTrucks", suggestedTrucks);
        }        
        
        //suggest drivers
        if (order.getAssignedTruck() != null) {
            float workingHoursLimit = calcMaxWorkingHoursThatDriverCanHave(routeInfo
                    .getEstimatedTime());

            Set<DriverModel> suggestedDrivers = driverService
                    .findUnassignedToTrucksDriversByMaxWorkingHoursAndCity(
                            order.getAssignedTruck().getCurrentCityId(),
                            workingHoursLimit);

            for (DriverModel d : suggestedDrivers) {
                d.setWorkingHoursThisMonth(driverService
                        .calculateWorkingHoursForDriver(d.getId()));
            }
            model.addAttribute("suggestedDrivers", suggestedDrivers);
        }
        
        model.addAttribute("statuses", OrderStatus.values());
        
        cityUtils.addCitiesToModel(model);
        return editOrderViewPath;
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
    public String addCargoToOrder(HttpServletRequest request,
            HttpServletResponse response) throws LogiwebServiceException {
        try {
            CargoModel newCargo = createDetachedCargoModelFromRequestParams(request);
            cargoService.addCargo(newCargo);
            return "Cargo added";
        } catch (FormParamaterParsingException | ServiceValidationException e) {
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
            OrderModel order = orderService.findOrderById(orderId);
            TruckModel truck = order.getAssignedTruck();
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
        int newOrderId = orderService.createNewEmptyOrder();
        return "redirect:/order/" + newOrderId + "/edit";
    }
    
    @RequestMapping(value = { "/order" })
    public String showOrders(Model model) throws LogiwebServiceException {
        model.addAttribute("orders", orderService.findAllOrders());
        cityUtils.addCitiesToModel(model);
        return orderListViewPath;
    }
    
    @RequestMapping(value = { "/cargo" })
    public ModelAndView showCargoes() throws LogiwebServiceException {
        ModelAndView mav = new ModelAndView();
        mav.setViewName(cargoesListViewPath);
        mav.addObject("cargoes", cargoService.findAllCargoes());
        return mav;
    }
    
    private CargoModel createDetachedCargoModelFromRequestParams(
            HttpServletRequest request) throws FormParamaterParsingException {
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
            originCityId = Integer.parseInt(request.getParameter("originCityId"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("Origin city (" + request.getParameter("originCity") + ") is in wrong format or null");
        }
        
        try {
            destinationCityId = Integer.parseInt(request.getParameter("destinationCityId"));
        } catch (NumberFormatException | NullPointerException e) {
            throw new FormParamaterParsingException("Destination city(" + request.getParameter("destinationCity") + ") is in wrong format or null");
        }
        
        CargoModel newCargo = new CargoModel();
        newCargo.setTitle(request.getParameter("cargoTitle"));
        newCargo.setOriginCityId(originCityId);
        newCargo.setDestinationCityId(destinationCityId);
        newCargo.setWeight(cargoWeight);
        newCargo.setOrderIdForThisCargo(orderId);
        
        return newCargo;
    }
}

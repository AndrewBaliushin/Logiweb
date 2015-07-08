package com.tsystems.javaschool.logiweb.controllers;

import java.util.HashMap;
import java.util.Map;

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
import com.tsystems.javaschool.logiweb.model.status.OrderStatus;
import com.tsystems.javaschool.logiweb.service.CityService;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.OrdersAndCargoService;
import com.tsystems.javaschool.logiweb.service.RouteService;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.RouteService.RouteInformation;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

@Controller
public class OrderAndCargoController {
    
    private final static Logger LOG = Logger.getLogger(OrderAndCargoController.class);

    private LogiwebAppContext ctx = LogiwebAppContext.INSTANCE;
    
    private CityService cityService = ctx.getCityService();
    private OrdersAndCargoService orderAndCaroService = ctx.getOrdersAndCargoService();
    private RouteService routeService = ctx.getRouteService();

    @RequestMapping(value = {"manager/editOrder"}, method = RequestMethod.GET)
    public ModelAndView editOrder(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("manager/EditOrder");
        
        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            DeliveryOrder order = orderAndCaroService.findOrderById(orderId);
            if (order == null) throw new IllegalArgumentException("Order #" + orderId + " not exist.");

            RouteInformation routeInfo = routeService
                    .getRouteInformationForOrder(order);

            mav.addObject("orderId", orderId);
            mav.addObject("order", order);
            mav.addObject("routeInfo", routeInfo);
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
    public String addCargo(HttpServletRequest request, HttpServletResponse response) {
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

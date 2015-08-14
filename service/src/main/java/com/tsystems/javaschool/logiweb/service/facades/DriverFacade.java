package com.tsystems.javaschool.logiweb.service.facades;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.DriverModel;
import com.tsystems.javaschool.logiweb.model.OrderModel;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.OrderService;
import com.tsystems.javaschool.logiweb.service.RouteService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;

@Component
public class DriverFacade {
    
    private DriverService driverService;
    private OrderService orderService;
    private RouteService routeService;
    
    @Autowired
    public DriverFacade(DriverService driverService, OrderService orderService,
            RouteService routeService) {
        this.driverService = driverService;
        this.orderService = orderService;
        this.routeService = routeService;
    }
    
    /**
     * Get fully formed Driver Model which will have all it's fields filled (if
     * possible).
     * 
     * Including: working hours, shift journals and route information.
     * 
     * @param driverId
     * @return
     * @throws LogiwebServiceException
     */
    @Transactional
    public DriverModel getDriverModelWithFullInfo(int driverId) throws LogiwebServiceException {
        DriverModel driver = driverService.findDriverById(driverId);
        if (driver == null) {
            return null;
        }
        
        driver.setWorkingHoursThisMonth(driverService
                .calculateWorkingHoursForDriver(driver.getId()));
        driver.setThisMonthShiftJurnals(driverService
                .findDriverJournalsForThisMonth(driver.getId()));

        if (driver.getOrderId() == null) {
            return driver;
        }
        
        OrderModel order = orderService.findOrderById(driver.getOrderId());
        RouteInformation routeInfo = routeService.getRouteInformationForOrder(order.getId());
        driver.setRouteInfo(routeInfo);
        
        return driver;
    }

}

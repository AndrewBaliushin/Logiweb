package com.tsystems.javaschool.logiweb.service;

import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;

/**
 * Service for analysis of Delivery Order. 
 * Helps in creation of optimal delivery routes and estimation of delivery time.
 * 
 * @author Andrey Baliushin
 */
public interface RouteService {
    
    /**
     * Analyze Order and return object with rout data.
     * 
     * @param order
     * @return {@link RouteInformation}
     */
    RouteInformation getRouteInformationForOrder(DeliveryOrder order);
}

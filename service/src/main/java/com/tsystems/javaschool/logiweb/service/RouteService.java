package com.tsystems.javaschool.logiweb.service;

import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;

/**
 * Service for analysis of Delivery Order. Helps in creation of optimal delivery
 * routes and estimation of delivery time.
 * 
 * @author Andrey Baliushin
 */
public interface RouteService {

    /**
     * Analyze Order and return object with route data.
     * 
     * @param orderId
     * @return {@link RouteInformation}
     * @throws LogiwebServiceException
     *             if something unepecte happend on lover level (not user fault)
     */
    RouteInformation getRouteInformationForOrder(int orderId)
            throws LogiwebServiceException;
}

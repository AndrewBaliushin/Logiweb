package com.tsystems.javaschool.logiweb.service;

import com.tsystems.javaschool.logiweb.model.DeliveryOrder;

public interface RouteService {

    /**
     * Estimate how much time will it take to deliver this order.
     * @param order
     * @return time in hours
     */
    float estimateTimeToDeliverOrder(DeliveryOrder order);
}

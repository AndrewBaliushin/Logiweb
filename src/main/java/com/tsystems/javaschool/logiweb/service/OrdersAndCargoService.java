package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.Cargo;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

public interface OrdersAndCargoService {
    
    /**
     * Find all orders.
     * 
     * @return orders set or empty set.
     * @throws LogiwebServiceException if something un
     */
    Set<DeliveryOrder> findAllOrders() throws LogiwebServiceException;
    
    void addNewOrder(DeliveryOrder newOrder) throws LogiwebServiceException;
    
    void findOrderById(int id) throws LogiwebServiceException;
    
    void addCargoToOrder(Cargo newCargo, int orderId) throws LogiwebServiceException;
    
    void setStatusOrderReadyToGo(DeliveryOrder order) throws LogiwebServiceException;
    
    void findAvailiableDriversForOrder(DeliveryOrder order) throws LogiwebServiceException;

    Set<Truck> findAvailiableTrucksForOrder(DeliveryOrder order) throws LogiwebServiceException;
}

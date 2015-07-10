package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.Cargo;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.model.status.OrderStatus;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

/**
 * Data manipulation and business logic related to Cargoes and Delivery Orders.
 * 
 * @author Andrey Baliushin
 */
public interface OrdersAndCargoService {
    
    /**
     * Find all orders.
     * 
     * @return orders set or empty set.
     * @throws LogiwebServiceException if something unexpected happens
     */
    Set<DeliveryOrder> findAllOrders() throws LogiwebServiceException;
    
    /**
     * Add new order. 
     * @param newOrder
     * @return same order
     * @throws LogiwebServiceException if something unexpected happens
     */
    DeliveryOrder addNewOrder(DeliveryOrder newOrder) throws LogiwebServiceException;
    
    /**
     * Find ofrder by id.
     * 
     * @param id
     * @return order or null if not found 
     * @throws LogiwebServiceException if something unexpected happens
     */
    DeliveryOrder findOrderById(int id) throws LogiwebServiceException;
    
    /**
     * Add new cargo. 
     * Cargo must contain title, origin and delivery cities, weight and order, to which it must be assigned.
     * @param newCargo
     * @throws LogiwebServiceException -- if unexpected happened
     * @throws ServiceValidationException -- if new cargo doesn't fit requirements
     */
    void addCargo(Cargo newCargo) throws ServiceValidationException, LogiwebServiceException;
    
    /**
     * Assign truck to order.
     * Truck must by managed entity.
     * 
     * @param truck
     * @param orderId
     * @throws ServiceValidationException if truck is not Free or broken.
     * @throws LogiwebServiceException if unexpected happened
     */
    void assignTruckToOrder(Truck truck, int orderId) throws ServiceValidationException, LogiwebServiceException;

    /**
     * Sets status for order.
     * 
     * @param status
     * @param order
     * @throws LogiwebServiceException if unexpected happened
     */
    void setStatusForOrder(OrderStatus status, DeliveryOrder order)
            throws LogiwebServiceException;
}

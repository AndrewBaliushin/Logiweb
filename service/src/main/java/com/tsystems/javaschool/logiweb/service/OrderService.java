package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

/**
 * Data manipulation and business logic related to Cargoes and Delivery Orders.
 * 
 * @author Andrey Baliushin
 */
public interface OrderService {

    /**
     * Find all orders.
     * 
     * @return orders set or empty set.
     * @throws LogiwebServiceException
     *             if something unexpected happens
     */
    Set<DeliveryOrder> findAllOrders() throws LogiwebServiceException;

    

    /**
     * Add new order.
     * 
     * @param newOrder
     * @return same order
     * @throws LogiwebServiceException
     *             if something unexpected happens
     */
    DeliveryOrder addNewOrder(DeliveryOrder newOrder)
            throws LogiwebServiceException;

    /**
     * Find ofrder by id.
     * 
     * @param id
     * @return order or null if not found
     * @throws LogiwebServiceException
     *             if something unexpected happens
     */
    DeliveryOrder findOrderById(int id) throws LogiwebServiceException;

    /**
     * Assign truck to order. Truck must by managed entity.
     * 
     * @param truck
     * @param orderId
     * @throws ServiceValidationException
     *             if truck is not Free or broken.
     * @throws LogiwebServiceException
     *             if unexpected happened
     */
    void assignTruckToOrder(Truck truck, int orderId)
            throws ServiceValidationException, LogiwebServiceException;

    /**
     * Sets 'READY' status for order if order have at least one cargo and assign
     * truck with full crew.
     * 
     * @param order
     * @throws LogiwebServiceException
     *             if unexpected happened
     * @throws ServiceValidationException
     *             if validation failed. Description in message.
     */
    void setReadyStatusForOrder(DeliveryOrder order)
            throws ServiceValidationException, LogiwebServiceException;

    /**
     * Sets 'DELIVERED' status for order if all cargoes for this order were
     * delivered.
     * Unassign truck and drivers from order.
     * 
     * @param order
     * @throws LogiwebServiceException
     *             if unexpected happened
     * @throws ServiceValidationException
     *             if validation failed. Description in message.
     *             Thrown if order have undelivered cargo.
     */
    void setStatusDeliveredForOrder(int orderId)
            throws ServiceValidationException, LogiwebServiceException;

    /**
     * Check if all cargoes were delivered and order can be set to Delivered.
     * 
     * @param orderId
     * @return true if order complete and false if there are undelivered cargoes
     *         inside order.
     * @throws LogiwebServiceException
     *             if validation failed. Description in message.
     */
    boolean isAllCargoesInOrderDelivered(int orderId) throws LogiwebServiceException;
}

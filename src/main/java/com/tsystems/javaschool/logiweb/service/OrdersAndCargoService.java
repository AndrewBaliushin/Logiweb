package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.Cargo;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Truck;

public interface OrdersAndCargoService {
    
    void findAllOrders() throws DaoException;
    
    void addNewOrder(DeliveryOrder newOrder) throws DaoException;
    
    void findOrderById(int id) throws DaoException;
    
    void addCargoToOrder(Cargo newCargo, int orderId) throws DaoException;
    
    void setStatusOrderReadyToGo(DeliveryOrder order) throws DaoException;
    
    void findAvailiableDriversForOrder(DeliveryOrder order) throws DaoException;

    Set<Truck> findAvailiableTrucksForOrder(DeliveryOrder order) throws DaoException;
}

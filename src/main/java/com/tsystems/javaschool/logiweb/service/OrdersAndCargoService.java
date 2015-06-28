package com.tsystems.javaschool.logiweb.service;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.Cargo;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;

public interface OrdersAndCargoService {
    
    void findAllOrders() throws DaoException;
    
    void addNewOrder(DeliveryOrder newOrder) throws DaoException;
    
    void findOrderById(int id) throws DaoException;
    
    void addCargoToOrder(Cargo newCargo, DeliveryOrder order) throws DaoException;
    
    void setStatusOrderReadyToGo(DeliveryOrder order) throws DaoException;

}

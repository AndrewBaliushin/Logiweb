package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Truck;

public interface TrucksService {

    Set<Truck> findAllTrucks() throws DaoException;

    Truck findTruckById(int id) throws DaoException;

    void editTruck(Truck editedTruck) throws DaoException;

    void addTruck(Truck newTruck) throws DaoException;
    
    void removeTruck(Truck truckToRemove) throws DaoException;
    
    Set<Truck> findAvailiableTrucksForOrder(DeliveryOrder order) throws DaoException;
}

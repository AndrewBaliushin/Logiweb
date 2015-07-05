package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import org.hibernate.metamodel.ValidationException;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

/**
 * Data manipulation and business logic related to Trucks.
 * 
 * @author Andrey Baliushin
 *
 */
public interface TrucksService {

    /**
     * Find trucks.
     * 
     * @return empty set if nothing found
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    Set<Truck> findAllTrucks() throws LogiwebServiceException;

    /**
     * Find truck by id.
     * 
     * @param id
     * @return truck or null
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    Truck findTruckById(int id) throws LogiwebServiceException;

    void editTruck(Truck editedTruck) throws LogiwebServiceException;

    /**
     * Add truck.
     * 
     * @param newTruck
     * @return same truck
     * @throws ServiceValidationException if truck don't have all required fields or not unique license plate
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    Truck addTruck(Truck newTruck) throws ServiceValidationException, LogiwebServiceException;
    
    /**
     * Remove truck.
     * 
     * @param truckToRemove
     * @throws ServiceValidationException if truck is attached to order or have attached drivers.
     * @throws LogiwebServiceException if unexpected exception on lower level occurred (not user fault)
     */
    void removeTruck(Truck truckToRemove) throws ServiceValidationException, LogiwebServiceException;
    
    Set<Truck> findAvailiableTrucksForOrder(DeliveryOrder order) throws LogiwebServiceException;
}

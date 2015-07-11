package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

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
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    Truck findTruckById(int id) throws LogiwebServiceException;

    /**
     * Edit truck.
     * 
     * @param editedTruck
     * @throws ServiceValidationException
     *             if truck don't have all required fields or have not unique license
     *             plate
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    void editTruck(Truck editedTruck) throws ServiceValidationException, LogiwebServiceException;

    /**
     * Add truck.
     * 
     * @param newTruck
     * @return same truck
     * @throws ServiceValidationException
     *             if truck don't have all required fields or not unique license
     *             plate
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    Truck addTruck(Truck newTruck) throws ServiceValidationException, LogiwebServiceException;
    
    /**
     * Remove truck.
     * 
     * @param truckToRemove
     * @throws ServiceValidationException
     *             if truck is attached to order or have attached drivers.
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    void removeTruck(Truck truckToRemove) throws ServiceValidationException, LogiwebServiceException;
    
    /**
     * Find trucks that have Status 'OK' and not busy by order, and have cargo
     * capacity (in tons) more or equal to minCargoWeightCapacity.
     * 
     * @param minCargoWeightCapacity
     * @return
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    Set<Truck> findFreeAndUnbrokenByCargoCapacity(float minCargoWeightCapacity) throws LogiwebServiceException;
    
    /**
     * Remove assignment to order and drivers for this truck.
     * 
     * @param truck
     * @throws LogiwebServiceException if unexpected exception on lower level occurred (not user fault)
     * @throws ServiceValidationException if truck is assigned to READY TO GO ORDER
     */
    void removeAssignedOrderAndDriversFromTruck(Truck truck) throws ServiceValidationException, LogiwebServiceException;
    
}

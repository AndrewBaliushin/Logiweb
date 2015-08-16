package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.TruckModel;
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
     * @return truck models or empty set if nothing found
     * @throws LogiwebServiceException if unexpected exception occurred on lower level (not user fault)
     */
    Set<TruckModel> findAllTrucks() throws LogiwebServiceException;

    /**
     * Find truck by id.
     * 
     * @param id
     * @return truck model or null
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    TruckModel findTruckById(int id) throws LogiwebServiceException;

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
    void editTruck(TruckModel editedTruckModel) throws ServiceValidationException, LogiwebServiceException;

    /**
     * Add truck.
     * 
     * @param newTruck as model
     * @return id of created truck
     * @throws ServiceValidationException
     *             if truck don't have all required fields or not unique license
     *             plate
     * @throws LogiwebServiceException
     *             if unexpected exception occurred on lower level (not user
     *             fault)
     */
    int addTruck(TruckModel newTruckModel) throws ServiceValidationException, LogiwebServiceException;
    
    /**
     * Remove truck.
     * 
     * @param trickId to remove
     * @throws ServiceValidationException
     *             if truck is attached to order or have attached drivers.
     * @throws LogiwebServiceException
     *             if unexpected exception on lower level occurred (not user
     *             fault)
     */
    void removeTruck(int truckId) throws ServiceValidationException, LogiwebServiceException;
    
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
    Set<TruckModel> findFreeAndUnbrokenByCargoCapacity(float minCargoWeightCapacity) throws LogiwebServiceException;
    
    /**
     * Remove assignment to order and drivers for this truck.
     * 
     * @param truckid
     * @throws LogiwebServiceException if unexpected exception on lower level occurred (not user fault)
     * @throws ServiceValidationException if truck is assigned to READY TO GO ORDER
     */
    void removeAssignedOrderAndDriversFromTruck(int truckId) throws ServiceValidationException, LogiwebServiceException;
    
}

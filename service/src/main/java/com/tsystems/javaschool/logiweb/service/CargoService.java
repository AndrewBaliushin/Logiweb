package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.model.CargoModel;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

public interface CargoService {

    /**
     * Set 'PICKED_UP' status to cargo if it is attached to 
     * 'READY_TO_GO' order and have status 'WAITING_FOR_PICKUP'
     * 
     * @param cargoId
     * @throws IllegalStateException if order or cargo statuses do not match requirements
     * @throws RecordNotFoundServiceException if cargo with this Id not exist
     * @throws LogiwebServiceException if something unexpected happened 
     */
    void setPickedUpStatus(int cargoId) throws IllegalStateException, RecordNotFoundServiceException,
            LogiwebServiceException;

    /**
     * Set 'DELIVERED' status to cargo if it is attached to 
     * 'READY_TO_GO' order and have status 'PICKED_UP'
     * 
     * @param cargoId
     * @throws IllegalStateException if order or cargo statuses do not match requirements
     * @throws RecordNotFoundServiceException if cargo with this Id not exist
     * @throws LogiwebServiceException if something unexpected happened 
     */
    void setDeliveredStatus(int cargoId) throws IllegalStateException, RecordNotFoundServiceException,
            LogiwebServiceException;

    Cargo findById(int cargoId) throws LogiwebServiceException;
    
    /**
     * Find all cargoes.
     * 
     * @return cargoes set or empty set.
     * @throws LogiwebServiceException
     *             if something unexpected happens
     */
    Set<Cargo> findAllCargoes() throws LogiwebServiceException;
    
    /**
     * Add new cargo. Cargo must contain title, origin and delivery cities,
     * weight and order, to which it must be assigned.
     * 
     * @param newCargo as model
     * @throws LogiwebServiceException
     *             -- if unexpected happened
     * @throws ServiceValidationException
     *             -- if new cargo doesn't fit requirements
     */
    void addCargo(CargoModel newCargo) throws ServiceValidationException,
            LogiwebServiceException;
}

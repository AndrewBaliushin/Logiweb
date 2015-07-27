package com.tsystems.javaschool.logiweb.service;

import java.util.Set;

import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

public interface CargoService {

    void setPickedUpStatus(int cargoId) throws RecordNotFoundServiceException,
            LogiwebServiceException;

    void setDeliveredStatus(int cargoId) throws RecordNotFoundServiceException,
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
     * @param newCargo
     * @throws LogiwebServiceException
     *             -- if unexpected happened
     * @throws ServiceValidationException
     *             -- if new cargo doesn't fit requirements
     */
    void addCargo(Cargo newCargo) throws ServiceValidationException,
            LogiwebServiceException;
}

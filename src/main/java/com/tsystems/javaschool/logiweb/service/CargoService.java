package com.tsystems.javaschool.logiweb.service;

import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;

public interface CargoService {

    void setPickedUpStatus(int cargoId) throws RecordNotFoundServiceException,
            LogiwebServiceException;

    void setDeliveredStatus(int cargoId) throws RecordNotFoundServiceException,
            LogiwebServiceException;

    Cargo findById(int cargoId) throws LogiwebServiceException;
}

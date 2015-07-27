package com.tsystems.javaschool.logiweb.service.impl;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.dao.CargoDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.status.CargoStatus;
import com.tsystems.javaschool.logiweb.service.CargoService;
import com.tsystems.javaschool.logiweb.service.OrdersAndCargoService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;

@Service
public class CargoServiceImpl implements CargoService {

    private static final Logger LOG = Logger
            .getLogger(OrdersAndCargoServiceImpl.class);

    private CargoDao cargoDao;

    @Autowired
    public CargoServiceImpl(CargoDao cargoDao) {
        this.cargoDao = cargoDao;
    }

    @Override
    @Transactional
    public void setPickedUpStatus(int cargoId) throws LogiwebServiceException {
        changeStatus(cargoId, CargoStatus.PICKED_UP);
    }

    @Override
    @Transactional
    public void setDeliveredStatus(int cargoId)
            throws RecordNotFoundServiceException, LogiwebServiceException {
        changeStatus(cargoId, CargoStatus.DELIVERED);
    }

    private void changeStatus(int cargoId, CargoStatus newStatus)
            throws RecordNotFoundServiceException, LogiwebServiceException {
        try {
            Cargo c = cargoDao.find(cargoId);
            if (c == null) {
                throw new RecordNotFoundServiceException();
            }

            c.setStatus(newStatus);
            cargoDao.update(c);
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public Cargo findById(int cargoId) throws LogiwebServiceException {
        try {
            return cargoDao.find(cargoId);
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

}

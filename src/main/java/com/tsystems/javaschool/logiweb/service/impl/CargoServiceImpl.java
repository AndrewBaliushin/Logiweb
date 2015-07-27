package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.dao.CargoDao;
import com.tsystems.javaschool.logiweb.dao.CityDao;
import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.status.CargoStatus;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.service.CargoService;
import com.tsystems.javaschool.logiweb.service.OrderService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

@Service
public class CargoServiceImpl implements CargoService {

    private static final Logger LOG = Logger
            .getLogger(OrderServiceImpl.class);

    private CargoDao cargoDao;
    private CityDao cityDao;
    private DeliveryOrderDao deliveryOrderDao;

    @Autowired
    public CargoServiceImpl(CargoDao cargoDao, CityDao cityDao,
            DeliveryOrderDao deliveryOrderDao) {
        this.cargoDao = cargoDao;
        this.cityDao = cityDao;
        this.deliveryOrderDao = deliveryOrderDao;
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Set<Cargo> findAllCargoes() throws LogiwebServiceException {
        try {
            return cargoDao.findAll();
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void addCargo(Cargo newCargo) throws ServiceValidationException, LogiwebServiceException {
        try {
            validateNewCargoForEmptyFields(newCargo);
        } catch (ServiceValidationException e) {
            throw e;
        }
        
        try {
            City originCity = cityDao.find(newCargo.getOriginCity().getId());
            City destinationCity = cityDao.find(newCargo.getDestinationCity()
                    .getId());
            DeliveryOrder orderForCargo = deliveryOrderDao.find(newCargo
                    .getOrderForThisCargo().getId());
            
            
            //switch detached entities in cargo to managed ones
            newCargo.setOriginCity(originCity);
            newCargo.setDestinationCity(destinationCity);
            newCargo.setOrderForThisCargo(orderForCargo);
            
            validateCargoManagedFieldsByBusinessRequirements(newCargo);            
            
            newCargo.setStatus(CargoStatus.WAITING_FOR_PICKUP);
            
            cargoDao.create(newCargo);
            LOG.info("New cargo with id #" + newCargo.getId() + "created for irder id #" + orderForCargo.getId());
        } catch (DaoException e) {         
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }
    
    /**
     * Validate that cities, title, weight, and order fields are not empty or null.
     * @param c
     * @throws ServiceValidationException if validation failed.
     */
    private void validateNewCargoForEmptyFields(Cargo c) throws ServiceValidationException {
        if (StringUtils.isBlank(c.getTitle())) {
            throw new ServiceValidationException("Cargo title can't be blank.");
        } else if (c.getWeight() <= 0f) {
            throw new ServiceValidationException("Cargo weight must be greater than zero.");
        } else if (c.getOriginCity() == null) {
            throw new ServiceValidationException("Origin city must be specified.");
        } else if (c.getDestinationCity() == null) {
            throw new ServiceValidationException("Desitnation city must be specified.");
        } else if (c.getOrderForThisCargo() == null) {
            throw new ServiceValidationException("Delivery Order must be specified.");
        }
    }
    
    /**
     * Validate cargo (fields must be managed)
     * 
     * Req.:
     * Both destination an origin cities must exist, and must not be the same city.
     * Order for this cargo must exist.
     * Order must be with status NOT READY
     * @param c cargo where all fields are managed entities.
     * @throws ServiceValidationException if requirements are not met. 
     */
     private void validateCargoManagedFieldsByBusinessRequirements(Cargo c) throws ServiceValidationException {
         if (c.getOriginCity() == null) {
             throw new ServiceValidationException("Origin city does not exist.");
         } else if (c.getDestinationCity() == null) {
             throw new ServiceValidationException(
                     "Destination city does not exist.");
         } else if (c.getOriginCity().equals(c.getDestinationCity())) {
             throw new ServiceValidationException("Cities must be different.");
         } else if (c.getOrderForThisCargo() == null) {
             throw new ServiceValidationException(
                     "Order for this cargo does not exist.");
         } else if (c.getOrderForThisCargo().getStatus() != OrderStatus.NOT_READY) {
             throw new ServiceValidationException(
                     "Order must be in NOT READY status to add new cargo.");
         } else if (c.getOrderForThisCargo().getAssignedTruck() != null) {
             throw new ServiceValidationException(
                     "Order can't be assigned to Truck.");
         }
     }
     

}

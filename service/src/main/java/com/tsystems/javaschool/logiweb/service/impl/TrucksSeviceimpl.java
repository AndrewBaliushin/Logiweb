package com.tsystems.javaschool.logiweb.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.DriverStatus;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.entities.status.TruckStatus;
import com.tsystems.javaschool.logiweb.model.TruckModel;
import com.tsystems.javaschool.logiweb.model.ext.ModelToEntityConverter;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.service.validators.LicensePlateValidator;

/**
 * Data manipulation and business logic related to Trucks.
 * 
 * @author Andrey Baliushin
 */
@Service
public class TrucksSeviceimpl implements TrucksService {
    
    private static final Logger LOG = Logger.getLogger(TrucksSeviceimpl.class);
    
    private TruckDao truckDao;
    
    private LicensePlateValidator licenserPlateValidator;
    
    @Autowired
    public TrucksSeviceimpl(TruckDao truckDao, LicensePlateValidator licenserPlateValidator) {
	this.truckDao = truckDao;
	this.licenserPlateValidator = licenserPlateValidator;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Set<TruckModel> findAllTrucks() throws LogiwebServiceException {
        try {
            return ModelToEntityConverter.convertTrucksToModels(truckDao
                    .findAll());
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     * @throws LogiwebServiceException 
     */
    @Override
    @Transactional
    public TruckModel findTruckById(int id) throws LogiwebServiceException {
        try {
            Truck t = truckDao.find(id);
            if (t == null) {
                return null;
            } else {
                return ModelToEntityConverter.convertToModel(t);
            }
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
    public void editTruck(TruckModel editedTruckModel) throws ServiceValidationException, LogiwebServiceException {
        if(editedTruckModel.getId() == null || editedTruckModel.getId() <= 0) {
            throw new ServiceValidationException("Truck id is not provided or incorrect.");
        }
        
        if (!licenserPlateValidator.validateLicensePlate(editedTruckModel
                .getLicencePlate())) {
            throw new ServiceValidationException("License plate "
                    + editedTruckModel.getLicencePlate() + " is not valid.");
        }
        
        try {
            Truck truckWithSamePlate = truckDao.findByLicensePlate(editedTruckModel.getLicencePlate());
            
            if (truckWithSamePlate != null && truckWithSamePlate.getId() != editedTruckModel.getId()) {
                throw new ServiceValidationException("License plate "
                        + editedTruckModel.getLicencePlate() + " is already in use.");
            }
            
            Truck truckEntityToEdit = truckDao.find(editedTruckModel.getId());
            if (truckEntityToEdit == null) {
                throw new ServiceValidationException("Truck with id " + editedTruckModel.getId() + " not found.");
            }
            
            if (truckEntityToEdit.getAssignedDeliveryOrder() != null) {
                throw new ServiceValidationException("Can't edit truck while order is assigned.");
            }
            
            populateAllowedFieldsFromModel(truckEntityToEdit, editedTruckModel);
            validateForEmptyFields(truckEntityToEdit);
            
            truckDao.update(truckEntityToEdit);

            LOG.info("Truck edited. Plate " + truckEntityToEdit.getLicencePlate()
                    + " ID: " + truckEntityToEdit.getId());
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }
    
    private Truck populateAllowedFieldsFromModel(Truck entityToEdit, TruckModel source) {
        City city = new City();
        city.setId(source.getCurrentCityId());
        
        entityToEdit.setCurrentCity(city);
        entityToEdit.setCargoCapacity(source.getCargoCapacity());
        entityToEdit.setCrewSize(source.getCrewSize());
        entityToEdit.setStatus(source.getStatus());
        entityToEdit.setLicencePlate(source.getLicencePlate());
        
        return entityToEdit;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int addTruck(TruckModel newTruckModel) throws ServiceValidationException,
            LogiwebServiceException {
        newTruckModel.setStatus(TruckStatus.OK); //default status
        
        if (!licenserPlateValidator.validateLicensePlate(newTruckModel
                .getLicencePlate())) {
            throw new ServiceValidationException("License plate "
                    + newTruckModel.getLicencePlate() + " is not valid.");
        }
        
        try {
            Truck truckWithSamePlate = truckDao
                    .findByLicensePlate(newTruckModel.getLicencePlate());
    
            if (truckWithSamePlate != null) {
                throw new ServiceValidationException("License plate "
                        + newTruckModel.getLicencePlate()
                        + " is already in use.");
            }
            
            Truck newTruckEntity = new Truck();
            populateAllowedFieldsFromModel(newTruckEntity, newTruckModel);
            validateForEmptyFields(newTruckEntity); 
        
            truckDao.create(newTruckEntity);
            LOG.info("Truck created. Plate " + newTruckEntity.getLicencePlate()
                    + " ID: " + newTruckEntity.getId());

            return newTruckEntity.getId();
        } catch (ServiceValidationException e) {
            throw e;        
        } catch (DaoException e) {         
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

    /**
     * Check if truck has empty fields that should not be empty.
     * @param t Truck
     * @return Doesn't return anything -- throws exception if failed.
     * @throws ServiceValidationException with message that describes why validation failed.
     */
    private void validateForEmptyFields(Truck t) throws ServiceValidationException {
        if(t.getLicencePlate() == null || t.getLicencePlate().isEmpty()) {
            throw new ServiceValidationException("License plate not set.");
        } else if (t.getCrewSize() <= 0) {
            throw new ServiceValidationException("Crew size can't be 0 or negative.");
        } else if (t.getCargoCapacity() <= 0) {
            throw new ServiceValidationException("Cargo size can't be 0 or negative.");
        } else if (t.getCurrentCity() == null || t.getCurrentCity().getId() == 0) {
            throw new ServiceValidationException("City is not set.");
        } 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void removeTruck(int truckId)
            throws ServiceValidationException, LogiwebServiceException {
        try {
            Truck truckToRemove = truckDao.find(truckId);
            if (truckToRemove == null) {
                throw new ServiceValidationException("Truck "
                        + truckId
                        + " not exist. Deletion forbiden.");
            } else if (truckToRemove.getAssignedDeliveryOrder() != null) {
                throw new ServiceValidationException(
                        "Truck is assigned to order. Deletion forbiden.");
            } else if (!truckToRemove.getDrivers().isEmpty()) {
                throw new ServiceValidationException(
                        "Truck is assigned to one or more drivers. "
                                + "Deletion forbiden.");
            }
            
            truckDao.delete(truckToRemove);
            
            LOG.info("Truck removed. Plate " + truckToRemove.getLicencePlate()
                    + " ID: " + truckToRemove.getId());
        } catch (ServiceValidationException e) {
            throw e;        
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
    public Set<TruckModel> findFreeAndUnbrokenByCargoCapacity(float minCargoWeightCapacity) throws LogiwebServiceException {
        try {
            return ModelToEntityConverter
                    .convertTrucksToModels(truckDao
                            .findByMinCapacityWhereStatusOkAndNotAssignedToOrder(minCargoWeightCapacity)); 
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
    public void removeAssignedOrderAndDriversFromTruck(int truckId)
            throws ServiceValidationException, LogiwebServiceException {
        try {
            Truck truck = truckDao.find(truckId);
            
            if (truck == null) {
                throw new ServiceValidationException("Truck not found.");
            }
            
            if (truck.getAssignedDeliveryOrder() == null) {
                throw new ServiceValidationException("Order is not assigned.");
            }
            
            if (truck.getAssignedDeliveryOrder().getStatus() == OrderStatus.READY_TO_GO) {
                throw new ServiceValidationException(
                        "Can't remove truck from READY TO GO order.");
            }
            
            DeliveryOrder order = truck.getAssignedDeliveryOrder();
            Set<Driver> drivers = truck.getDrivers();
            
            truck.setAssignedDeliveryOrder(null);
            truck.setDrivers(new HashSet<Driver>());
            
            for (Driver driver : drivers) {
                driver.setCurrentTruck(null);
                driver.setStatus(DriverStatus.FREE);
            }
            
            if(order != null) {
                order.setAssignedTruck(null);
            }
            
            truckDao.update(truck);            
            LOG.info("Truck id#" + truck.getId() + " and its drivers removed from order.");
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }
}

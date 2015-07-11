package com.tsystems.javaschool.logiweb.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.model.status.OrderStatus;
import com.tsystems.javaschool.logiweb.model.status.TruckStatus;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.utils.LicensePlateValidator;

/**
 * Data manipulation and business logic related to Trucks.
 * 
 * @author Andrey Baliushin
 */
public class TrucksSeviceimpl implements TrucksService {
    
    private static final Logger LOG = Logger.getLogger(TrucksSeviceimpl.class);

    private EntityManager em;
    
    private TruckDao truckDao;
    
    public TrucksSeviceimpl(TruckDao truckDao, EntityManager em) {
	this.em = em;
	this.truckDao = truckDao;
    }
    
    private EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Truck> findAllTrucks() throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Set<Truck> trucks = truckDao.findAll();
            getEntityManager().getTransaction().commit();
            return trucks;
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    /**
     * {@inheritDoc}
     * @throws LogiwebServiceException 
     */
    @Override
    public Truck findTruckById(int id) throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Truck truck = truckDao.find(id);
            getEntityManager().getTransaction().commit();
            return truck;
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void editTruck(Truck editedTruck){
	// TODO Auto-generated method stub

    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Truck addTruck(Truck newTruck) throws ServiceValidationException,
            LogiwebServiceException {
        newTruck.setStatus(TruckStatus.OK);
        
        try {
            validateForEmptyFields(newTruck); 
        } catch (ServiceValidationException e) {
            throw e;
        }

        if (!LicensePlateValidator.validateLicensePlate(newTruck
                .getLicencePlate())) {
            throw new ServiceValidationException("License plate "
                    + newTruck.getLicencePlate() + " is not valid.");
        }

        try {
            getEntityManager().getTransaction().begin();
            Truck truckWithSamePlate = truckDao.findByLicensePlate(newTruck.getLicencePlate());
            
            if (truckWithSamePlate != null) {
                throw new ServiceValidationException("License plate "
                        + newTruck.getLicencePlate() + " is already in use.");
            }
            
            truckDao.create(newTruck);
            getEntityManager().refresh(newTruck);
            getEntityManager().getTransaction().commit();

            LOG.info("Truck created. Plate " + newTruck.getLicencePlate()
                    + " ID: " + newTruck.getId());

            return newTruck;
        } catch (ServiceValidationException e) {
            throw e;        
        } catch (DaoException e) {         
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
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
    public void removeTruck(Truck truckToRemove)
            throws ServiceValidationException, LogiwebServiceException {
        int truckToRemoveId = truckToRemove.getId();
        try {
            getEntityManager().getTransaction().begin();

            truckToRemove = truckDao.find(truckToRemove.getId());
            if (truckToRemove == null) {
                throw new ServiceValidationException("Truck "
                        + truckToRemoveId
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
            getEntityManager().getTransaction().commit();

            LOG.info("Truck removed. Plate " + truckToRemove.getLicencePlate()
                    + " ID: " + truckToRemove.getId());
        } catch (ServiceValidationException e) {
            throw e;        
        } catch (DaoException e) {         
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Truck> findFreeAndUnbrokenByCargoCapacity(float minCargoWeightCapacity) throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Set<Truck> freeTrucks = truckDao.findByMinCapacityWhereStatusOkAndNotAssignedToOrder(minCargoWeightCapacity);
            getEntityManager().getTransaction().commit();
            
            return freeTrucks;
        } catch (DaoException e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAssignedOrderAndDriversFromTruck(Truck truck)
            throws ServiceValidationException, LogiwebServiceException {
        if (truck.getAssignedDeliveryOrder().getStatus() == OrderStatus.READY_TO_GO) {
            throw new ServiceValidationException(
                    "Can't remove truck from READY TO GO order.");
        }

        try {
            getEntityManager().getTransaction().begin();
            DeliveryOrder order = truck.getAssignedDeliveryOrder();
            Set<Driver> drivers = truck.getDrivers();
            
            truck.setAssignedDeliveryOrder(null);
            truck.setDrivers(new HashSet<Driver>());
            getEntityManager().merge(truck);
            
            for (Driver driver : drivers) {
                driver.setCurrentTruck(null);
                getEntityManager().merge(driver);
            }
            
            if(order != null) {
                order.setAssignedTruck(null);
            }
            
            getEntityManager().getTransaction().commit();
            
            LOG.info("Truck id#" + truck.getId() + " and its drivers removed from order.");
        } catch (Exception e) {
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }
}

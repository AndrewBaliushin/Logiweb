package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.CargoStatus;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.entities.status.TruckStatus;
import com.tsystems.javaschool.logiweb.model.OrderModel;
import com.tsystems.javaschool.logiweb.model.ext.ModelToEntityConverter;
import com.tsystems.javaschool.logiweb.service.OrderService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

/**
 * Data manipulation and business logic related to 
 * orders and cargoes.
 * 
 * @author Andrey Baliushin
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = Logger.getLogger(OrderServiceImpl.class);
        
    private DeliveryOrderDao deliveryOrderDao;
    private TruckDao truckDao;

    @Autowired
    public OrderServiceImpl(DeliveryOrderDao deliveryOrderDao, TruckDao truckDao) {
        this.deliveryOrderDao = deliveryOrderDao;
        this.truckDao = truckDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<OrderModel> findAllOrders() throws LogiwebServiceException {
        try {
            return ModelToEntityConverter
                    .convertOrdersToModels(deliveryOrderDao.findAll());
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
    public int createNewEmptyOrder()
            throws LogiwebServiceException {
        try {
            DeliveryOrder newOrder = new DeliveryOrder();
            newOrder.setStatus(OrderStatus.NOT_READY);
            deliveryOrderDao.create(newOrder);
            LOG.info("Order created. ID#" + newOrder.getId());
            return newOrder.getId();
        } catch (DaoException e) {         
            LOG.warn("Something unexpected happend.", e);
            throw new LogiwebServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderModel findOrderById(int id) throws LogiwebServiceException {
        try {
            DeliveryOrder o = deliveryOrderDao.find(id);
            if (o == null) {
                return null;
            } else {
                return ModelToEntityConverter.convertToModel(o);
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
    public void assignTruckToOrder(int truckId, int orderId)
            throws ServiceValidationException, LogiwebServiceException {
        try {
            Truck truck = truckDao.find(truckId); 
            if(truck == null) {
                throw new ServiceValidationException("Truck does not exist.");
            } 
            
            if(truck.getStatus() != TruckStatus.OK) {
                throw new ServiceValidationException("Truck must have OK status.");
            } else if (truck.getAssignedDeliveryOrder() != null ) {
                throw new ServiceValidationException("Truck must not have assigned orders.");
            }
            
            DeliveryOrder order = deliveryOrderDao.find(orderId);
            
            if(order == null) {
                throw new ServiceValidationException("Order " + orderId + " does not exist.");
            } else if (order.getAssignedTruck() != null) {
                throw new ServiceValidationException("Order " + orderId + " must not be assigned to another truck.");
            } else if (order.getAssignedCargoes() == null
                    || order.getAssignedCargoes().isEmpty()) {
                throw new ServiceValidationException("Order " + orderId
                        + " must have cargo.");
            }
            
            truck.setAssignedDeliveryOrder(order);
            order.setAssignedTruck(truck);
            
            deliveryOrderDao.update(order);
            truckDao.update(truck);
            LOG.info("Truck id#" + truck.getId() + " assign to order id#" + order.getId());
        } catch (DaoException e) {
            LOG.warn("Something unexpected happened.", e);
            throw new LogiwebServiceException(e);
        }        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void setReadyStatusForOrder(int orderId)
            throws ServiceValidationException, LogiwebServiceException {
        try {
            DeliveryOrder order = deliveryOrderDao.find(orderId); 
            
            if (order == null) {
                throw new ServiceValidationException("Order does not exist.");
            }
            
            if (order.getAssignedCargoes() == null
                    || order.getAssignedCargoes().isEmpty()) {
                throw new ServiceValidationException(
                        "Order must contain at least 1 cargo.");
            } else if (order.getAssignedTruck() == null) {
                throw new ServiceValidationException(
                        "Order must have assigned truck.");
            } else if (order.getAssignedTruck().getDrivers() == null
                    || order.getAssignedTruck().getDrivers().size() < order
                            .getAssignedTruck().getCrewSize()) {
                throw new ServiceValidationException(
                        "Truck must have full crew. Assign drivers.");
            } else if (order.getStatus() != OrderStatus.NOT_READY) {
                throw new ServiceValidationException(
                        "Order must be in NOT READY STATE.");
            }
            
            order.setStatus(OrderStatus.READY_TO_GO);
            deliveryOrderDao.update(order);
            LOG.info("Order id#" + order.getId() + " changed status to " + OrderStatus.READY_TO_GO);
        } catch (DaoException e) {
            LOG.warn("Something unexpected happened.", e);
            throw new LogiwebServiceException(e);
        }
    }

    @Override
    @Transactional
    public boolean isAllCargoesInOrderDelivered(int orderId)
            throws RecordNotFoundServiceException, LogiwebServiceException {
        try {
            DeliveryOrder order = deliveryOrderDao.find(orderId);
            if (order == null) {
                throw new RecordNotFoundServiceException();
            }
            
            Set<Cargo> cargoes = order.getAssignedCargoes();
            for (Cargo cargo : cargoes) {
                if (cargo.getStatus() != CargoStatus.DELIVERED) {
                    return false;
                }
            }
            return true;
        } catch (DaoException e) {
            LOG.warn("Something unexpected happened.", e);
            throw new LogiwebServiceException(e);
        }    
    }

    @Override
    @Transactional
    public void setStatusDeliveredForOrder(int orderId)
            throws ServiceValidationException, LogiwebServiceException {
        try {
            DeliveryOrder order = deliveryOrderDao.find(orderId);
            if(order == null) {
                throw new ServiceValidationException("Order does not exist.");
            }
            
            if (isAllCargoesInOrderDelivered(orderId)) {
                order.setStatus(OrderStatus.DELIVERED);
                deliveryOrderDao.update(order);
                LOG.info("Order id#" + order.getId() + " changed status to "
                        + OrderStatus.DELIVERED);
            } else {
                throw new ServiceValidationException(
                        "Order have undelivered cargo.");
            }
        } catch (DaoException e) {
            LOG.warn("Something unexpected happened.", e);
            throw new LogiwebServiceException(e);
        }    
    }
}

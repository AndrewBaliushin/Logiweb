package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.CargoDao;
import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.Cargo;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.service.OrdersAndCargoService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;

/**
 * Data manipulation and business logic related to 
 * orders and cargoes.
 * 
 * @author Andrey Baliushin
 */
public class OrdersAndCargoServiceImpl implements OrdersAndCargoService {

    private static final Logger LOG = Logger.getLogger(OrdersAndCargoServiceImpl.class);
        
    private DeliveryOrderDao deliveryOrderDao;
    private CargoDao cargoDao;
    private EntityManager em;

    public OrdersAndCargoServiceImpl(DeliveryOrderDao deliveryOrderDao,
            CargoDao cargoDao, EntityManager em) {
        this.deliveryOrderDao = deliveryOrderDao;
        this.cargoDao = cargoDao;
        this.em = em;
    }

    
    private EntityManager getEntityManager() {
        return em;
    }


    @Override
    public Set<DeliveryOrder> findAllOrders() throws LogiwebServiceException {
        try {
            getEntityManager().getTransaction().begin();
            Set<DeliveryOrder> orders = deliveryOrderDao.findAll();
            getEntityManager().getTransaction().commit();
            return orders;
        } catch (DaoException e) {
            LOG.warn("Something unexcpected happend.");
            throw new LogiwebServiceException(e);
        } finally {
            if (getEntityManager().getTransaction().isActive()) {
                getEntityManager().getTransaction().rollback();
            }
        }
    }

    @Override
    public void addNewOrder(DeliveryOrder newOrder)
            throws LogiwebServiceException {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void findOrderById(int id) throws LogiwebServiceException {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void addCargoToOrder(Cargo newCargo, int orderId)
            throws LogiwebServiceException {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void setStatusOrderReadyToGo(DeliveryOrder order)
            throws LogiwebServiceException {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void findAvailiableDriversForOrder(DeliveryOrder order)
            throws LogiwebServiceException {
        // TODO Auto-generated method stub
        
    }


    @Override
    public Set<Truck> findAvailiableTrucksForOrder(DeliveryOrder order)
            throws LogiwebServiceException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}

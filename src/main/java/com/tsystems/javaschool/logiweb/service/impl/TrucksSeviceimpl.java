package com.tsystems.javaschool.logiweb.service.impl;

import java.util.Set;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.service.TrucksService;

public class TrucksSeviceimpl extends GenericServiceImpl implements TrucksService {
    
    private TruckDao truckDao;
    
    public TrucksSeviceimpl(TruckDao truckDao, EntityManager em) {
	super(em);
	this.truckDao = truckDao;
    }

    @Override
    public Set<Truck> findAllTrucks() throws DaoException {
	getEntityManager().getTransaction().begin();
	Set<Truck> trucks = truckDao.findAll();
	getEntityManager().getTransaction().commit();
	return trucks;
    }

    @Override
    public Truck findTruckById(int id) throws DaoException {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void editTruck(Truck editedTruck) throws DaoException {
	// TODO Auto-generated method stub

    }

    @Override
    public void addTruck(Truck newTruck) throws DaoException {
	// TODO Auto-generated method stub

    }

    @Override
    public void removeTruck(Truck truckToRemove) throws DaoException {
	// TODO Auto-generated method stub

    }

    @Override
    public Set<Truck> findAvailiableTrucksForOrder(DeliveryOrder order)
	    throws DaoException {
	// TODO Auto-generated method stub
	return null;
    }

}

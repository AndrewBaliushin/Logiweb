package com.tsystems.javaschool.logiweb.dao.jpa;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.CargoDao;
import com.tsystems.javaschool.logiweb.dao.CityDao;
import com.tsystems.javaschool.logiweb.dao.DaoFactory;
import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.DriverShiftJournaDao;
import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.model.Cargo;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.DriverShiftJournal;
import com.tsystems.javaschool.logiweb.model.Truck;

/**
 * Factory for creation JPA DAO objects.
 * 
 * @author Andrey Baliushin
 */
public class DaoFactoryJpa implements DaoFactory {
    
    private EntityManager entityManager;

    public DaoFactoryJpa(EntityManager em) {
	entityManager = em;
    }
    
    @Override
    public CargoDao getCargoDao() {
	return new CargoDaoJpa(Cargo.class, entityManager);
    }

    @Override
    public CityDao getCityDao() {
	return new CityDaoJpa(City.class, entityManager);
    }

    @Override
    public DeliveryOrderDao getDeliveryOrderDao() {
	return new DeliveryOrderDaoJpa(DeliveryOrder.class, entityManager);
    }

    @Override
    public DriverDao getDriverDao() {
	return new DriverDaoJpa(Driver.class, entityManager);
    }

    @Override
    public DriverShiftJournaDao geDriverShiftJournaDao() {
	return new DriverShiftJournalDaoJpa(DriverShiftJournal.class, entityManager);
    }

    @Override
    public TruckDao getTruckDao() {
	return new TruckDaoJpa(Truck.class, entityManager);
    }

}

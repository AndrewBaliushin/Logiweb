package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.Set;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.Truck;

public class TruckDaoJpa extends GenericDaoJpa<Truck> implements TruckDao {

    public TruckDaoJpa(Class<Truck> entityClass, EntityManager entityManager) {
	super(entityClass, entityManager);
	// TODO Auto-generated constructor stub
    }

    @Override
    public Set<Truck> getAvailiableTrucksByCapacity(Float minCargoCapacity) {
	// TODO Auto-generated method stub
	return null;
    }


}

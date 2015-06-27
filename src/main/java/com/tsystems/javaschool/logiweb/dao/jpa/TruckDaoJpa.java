package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.model.status.TruckStatus;

public class TruckDaoJpa extends GenericDaoJpa<Truck> implements TruckDao {

    public TruckDaoJpa(Class<Truck> entityClass, EntityManager entityManager) {
	super(entityClass, entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Truck> findAvailiableTrucksByCapacity(float minCargoCapacity) {
	EntityManager em = getEntityManager();
	Class<Truck> truckEntityClass = getEntityClass();
	String classSimpleName = truckEntityClass.getSimpleName();

	Query query = em
	        .createQuery("SELECT t FROM "
	                + classSimpleName
	                + " t WHERE status = :status AND assignedDeliveryOrder IS NULL AND  cargoCapacity >= :capacity", truckEntityClass);
	query.setParameter("status", TruckStatus.OK);
	query.setParameter("capacity", minCargoCapacity);

	List<Truck> resultList = query.getResultList();
	Set<Truck> resultSet = new HashSet<Truck>(resultList);
	
	return resultSet;
    }

}

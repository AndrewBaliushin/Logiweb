package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoExceptionCode;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;

public class DriverDaoJpa extends GenericDaoJpa<Driver> implements DriverDao {

    public DriverDaoJpa(Class<Driver> entityClass, EntityManager entityManager) {
	super(entityClass, entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Driver> findByMaxWorkingHoursAndCityWhereNotAssignedToTruck(float maxWorkingHours, City inCity) {
	// TODO Auto-generated method stub
	return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Driver findByEmployeeId(int id) {
	EntityManager em = getEntityManager();

	Query query = em
	        .createQuery("SELECT t FROM "
	                + getEntityClass().getSimpleName()
	                + " t WHERE employeeId = :id", getEntityClass());
	query.setParameter("id", id);
	
	/*
	 * getResultList used instead of getSingleResult intentionally.
	 * Article on this:
	 * http://sysout.be/2011/03/09/why-you-should-never-use-getsingleresult-in-jpa/
	 */
	List<Driver> result = query.getResultList();
	
	Driver driver = null;
	if(result.size() > 0) {
	    driver = result.get(0);    
	} 
	return driver;
    }

}

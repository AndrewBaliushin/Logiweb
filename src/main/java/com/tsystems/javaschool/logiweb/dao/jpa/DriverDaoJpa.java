package com.tsystems.javaschool.logiweb.dao.jpa;

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
    public Set<Driver> findAvailiableToDrive(float maxWorkingHours, City inCity) {
	// TODO Auto-generated method stub
	return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Driver getByEmployeeId(int id) throws DaoException {
	EntityManager em = getEntityManager();

	Query query = em
	        .createQuery("SELECT t FROM "
	                + getEntityClass().getSimpleName()
	                + " t WHERE employeeId = :id", getEntityClass());
	query.setParameter("id", id);

	try {
	    Driver driver = (Driver) query.getSingleResult();
	    return driver;
	} catch (NoResultException e) {
	    throw new DaoException(DaoExceptionCode.NO_RESULT,
		    "Driver with Employee Id (" + id + ") is not found.");
	    
	} catch (NonUniqueResultException e) {
	    throw new DaoException(DaoExceptionCode.RESULT_IS_NOT_UNIQ,
		    "Driver with Employee Id (" + id + ") is not uniq.");
	}
    }

}

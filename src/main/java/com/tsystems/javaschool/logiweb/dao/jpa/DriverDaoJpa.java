package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoExceptionCode;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;

/**
 * CRUD operations for Driver entity (JPA implementation).
 * 
 * @author Andrey Baliushin
 */
public class DriverDaoJpa extends GenericDaoJpa<Driver> implements DriverDao {

    private static final Logger LOG = Logger.getLogger(GenericDaoJpa.class);
    
    public DriverDaoJpa(Class<Driver> entityClass, EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Driver> findByMaxWorkingHoursAndCityWhereNotAssignedToTruck(
            float maxWorkingHours, City inCity) throws DaoException {
        // TODO implement this method
        return new HashSet<Driver>(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Driver findByEmployeeId(int id) throws DaoException {
        try {
            EntityManager em = getEntityManager();

            Query query = em.createQuery("SELECT t FROM "
                    + getEntityClass().getSimpleName()
                    + " t WHERE employeeId = :id", getEntityClass());
            query.setParameter("id", id);

            /*
             * getResultList used instead of getSingleResult intentionally.
             * Article on this:
             * http://sysout.be/2011/03/09/why-you-should-never-
             * use-getsingleresult-in-jpa/
             * 
             * Cause of suppression: type List needs unchecked conversion to
             * conform to List<Driver>
             */
            @SuppressWarnings("unchecked")
            List<Driver> result = query.getResultList();

            Driver driver = null;
            if (!result.isEmpty()) {
                driver = result.get(0);
            }
            return driver;
        } catch (Exception e) {
            LOG.warn("Failed to find entity " + getEntityClass()
                    + " by ID = " + id + ". Exception msg: " + e.getMessage());
            throw new DaoException(DaoExceptionCode.SEARCH_FAILED);
        }
    }

}

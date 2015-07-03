package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
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
            LOG.warn(e);
            throw new DaoException(e);
        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Driver> findByCityWhereNotAssignedToTruck(City city) throws DaoException {
        try {
            EntityManager em = getEntityManager();

            Query query = em.createQuery("SELECT t FROM "
                    + getEntityClass().getSimpleName() + " t"
                    + " WHERE currentTruck IS NULL"
                    + " AND currentCity = :city", getEntityClass());
            query.setParameter("city", city);

            /*
             * Cause of suppression: type List needs unchecked conversion to
             * conform to List<Driver>
             */
            @SuppressWarnings("unchecked")
            List<Driver> result = query.getResultList();

            return new HashSet<Driver>(result);
        } catch (Exception e) {
            LOG.warn(e);
            throw new DaoException(e);
        }
    }
    
}

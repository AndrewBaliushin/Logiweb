package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.GenericDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoExceptionCode;

/**
 * Abstract class with set of basic CRUD operations used as superclass
 * by all DaoJpa objects.
 * 
 * @author Andrey Baliushin
 */
public abstract class GenericDaoJpa<T> implements GenericDao<T> {

    private static final Logger LOG = Logger.getLogger(GenericDaoJpa.class);

    private Class<T> entityClass;
    private EntityManager entityManager;

    public GenericDaoJpa(Class<T> entityClass, EntityManager entityManager) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    /**
     * Get entity class that were used for creation of this DAO object.
     * 
     * @return class of used entity
     */
    protected final Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * Get EntityManger object that was used for creation of this DAO object.
     * 
     * @return EntityManager object
     */
    protected final EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T create(T newInstance) throws DaoException {
        if (newInstance == null) {
            return null; // TODO make sure it's right way
        }
        
        try {
            entityManager.persist(newInstance);
            return newInstance;
        } catch (Exception e) {
            LOG.warn("Failed to create entity " + getEntityClass()
                    + ". Exception msg: " + e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T find(int id) throws DaoException {
        try {
            return getEntityManager().find(getEntityClass(), id);
        } catch (Exception e) {
            LOG.warn("Failed to find entity " + getEntityClass()
                    + " by ID = " + id + ". Exception msg: " + e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void update(T changedObject) throws DaoException {
        if (changedObject == null) {
            return; // TODO make sure it's right way
        }

        try {
            getEntityManager().merge(changedObject);
        } catch (Exception e) {
            LOG.warn("Failed to update entity " + getEntityClass()
                    + ". Exception msg: " + e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void delete(T objectToDelete) throws DaoException {
        if (objectToDelete == null) {
            return; // TODO make sure it's right way

        }

        try {
            getEntityManager().remove(objectToDelete);
        } catch (Exception e) {
            LOG.warn("Failed to delete entity " + getEntityClass()
                    + ". Exception msg: " + e.getMessage());
            throw new DaoException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Set<T> findAll() throws DaoException {
        try {
            // unchecked conversion to conform to List<T>
            @SuppressWarnings("unchecked")
            List<T> allEntities = getEntityManager().createQuery(
                    "Select t from " + getEntityClass().getSimpleName() + " t")
                    .getResultList();

            return new HashSet<T>(allEntities);
        } catch (Exception e) {
            LOG.warn("Failed to FindAll entities for: " + getEntityClass()
                    + ". Exception msg: " + e.getMessage());
            throw new DaoException(e);
        }
    }

}

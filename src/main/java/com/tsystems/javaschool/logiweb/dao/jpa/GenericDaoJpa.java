package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.GenericDao;

public abstract class GenericDaoJpa<T> implements GenericDao<T> {
    
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
     * Get  EntityManger object that were used for creation of this DAO object.
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
    public final T create(T newInstance) {
	entityManager.persist(newInstance);
	return newInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T read(Integer id) {
	T entity = getEntityManager().find(getEntityClass(), id);
	return entity;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void update(T changedObject) {
	getEntityManager().merge(changedObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void delete(T objectToDelete) {
	getEntityManager().remove(objectToDelete);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public final Set<T> findAll() {
	List<T> allEntities = getEntityManager().createQuery(
	        "Select t from " + getEntityClass().getSimpleName() + " t")
	        .getResultList();
	
	if (allEntities.isEmpty()) {
	    return null;
	} else {
	    Set<T> allEntitiesAsSet = new HashSet<T>(allEntities);
	    return allEntitiesAsSet;
	}
    }

}

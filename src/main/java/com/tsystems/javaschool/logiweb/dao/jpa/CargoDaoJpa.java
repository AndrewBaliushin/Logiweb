package com.tsystems.javaschool.logiweb.dao.jpa;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.CargoDao;
import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.model.Cargo;

public class CargoDaoJpa extends GenericDaoJpa<Cargo> implements CargoDao {

    public CargoDaoJpa(Class<Cargo> entityClass, EntityManager entityManager) {
	super(entityClass, entityManager);
    }

}

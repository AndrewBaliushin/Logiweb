package com.tsystems.javaschool.logiweb.dao.jpa;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.DriverShiftJournaDao;
import com.tsystems.javaschool.logiweb.model.DriverShiftJournal;

public class DriverShiftJournalDaoJpa extends GenericDaoJpa<DriverShiftJournal>
        implements DriverShiftJournaDao {

    public DriverShiftJournalDaoJpa(Class<DriverShiftJournal> entityClass,
            EntityManager entityManager) {
	super(entityClass, entityManager);
    }

}

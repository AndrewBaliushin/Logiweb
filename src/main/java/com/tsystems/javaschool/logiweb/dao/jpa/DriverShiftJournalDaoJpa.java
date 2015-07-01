package com.tsystems.javaschool.logiweb.dao.jpa;

import javax.persistence.EntityManager;

import com.tsystems.javaschool.logiweb.dao.DriverShiftJournaDao;
import com.tsystems.javaschool.logiweb.model.DriverShiftJournal;

/**
 * CRUD operations for Driver Shift Journal entity (JPA implementation).
 * 
 * @author Andrey Baliushin
 */
public class DriverShiftJournalDaoJpa extends GenericDaoJpa<DriverShiftJournal>
        implements DriverShiftJournaDao {

    public DriverShiftJournalDaoJpa(Class<DriverShiftJournal> entityClass,
            EntityManager entityManager) {
	super(entityClass, entityManager);
    }

}

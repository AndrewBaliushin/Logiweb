package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.tsystems.javaschool.logiweb.dao.DriverShiftJournaDao;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.DriverShiftJournal;
import com.tsystems.javaschool.logiweb.utils.DateUtils;

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

    @Override
    public Set<DriverShiftJournal> findThisMonthJournalsForDrivers(
            Set<Driver> drivers) {
        EntityManager em = getEntityManager();
        String journalEntityName = DriverShiftJournal.class.getSimpleName();
    
        String queryString = "SELECT j FROM " + journalEntityName + " j"
                + " WHERE driverForThisRecord IN :drivers"
                + " AND ( (shiftEnded BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth)"
                + " OR (shiftBeggined BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth) )";
        
        Query query = em.createQuery(queryString, DriverShiftJournal.class);
        query.setParameter("drivers", drivers);
        query.setParameter("firstDayOfMonth", DateUtils.getFirstDateOfCurrentMonth());
        query.setParameter("firstDayOfNextMonth", DateUtils.getFirstDayOfNextMonth());
    
        /*
         * type List needs unchecked conversion to conform to
         * List<DriverShiftJournal>
         */
        @SuppressWarnings("unchecked")
        List<DriverShiftJournal> result = query.getResultList();
    
        return new HashSet<DriverShiftJournal>(result);
    }

    @Override
    public Set<DriverShiftJournal> findThisMonthJournalsForDrivers(Driver driver) {
        Set<Driver> drivers = new HashSet<Driver>();
        drivers.add(driver);
        return findThisMonthJournalsForDrivers(drivers);
    }
    
    

}

package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.tsystems.javaschool.logiweb.dao.DriverShiftJournaDao;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<DriverShiftJournal> findThisMonthJournalsForDrivers(
            Set<Driver> drivers) {
        if(drivers == null || drivers.isEmpty()) {
            return new HashSet<DriverShiftJournal>(0);
        }
        
        EntityManager em = getEntityManager();
        String journalEntityName = DriverShiftJournal.class.getSimpleName();
        
        Date firstDateOfCurrentMonth = DateUtils.getFirstDateOfCurrentMonth();
        Date firstDateOfNextMonth = DateUtils.getFirstDayOfNextMonth();
    
        String queryString = "SELECT j FROM " + journalEntityName + " j"
                + " WHERE driverForThisRecord IN :drivers"
                + " AND ( (shiftEnded BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth)"
                + " OR (shiftBeggined BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth) )";

        Query query = em.createQuery(queryString, DriverShiftJournal.class)
                .setHint("org.hibernate.cacheable", false);     //fix for strange behavior of hiber
        query.setParameter("drivers", drivers);
        query.setParameter("firstDayOfMonth", firstDateOfCurrentMonth);
        query.setParameter("firstDayOfNextMonth", firstDateOfNextMonth);
    
        /*
         * type List needs unchecked conversion to conform to
         * List<DriverShiftJournal>
         */
        @SuppressWarnings("unchecked")
        List<DriverShiftJournal> result = query.getResultList();
    
        return new HashSet<DriverShiftJournal>(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<DriverShiftJournal> findThisMonthJournalsForDrivers(Driver driver) {
        Set<Driver> drivers = new HashSet<Driver>();
        drivers.add(driver);
        return findThisMonthJournalsForDrivers(drivers);
    }
    
    

}

package com.tsystems.javaschool.logiweb.dao.jpa;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.tsystems.javaschool.logiweb.dao.DriverDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoExceptionCode;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.DriverShiftJournal;
import com.tsystems.javaschool.logiweb.utils.DateUtils;

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
        EntityManager em = getEntityManager();

        Set<DriverShiftJournal> journals =
                findThisMonthJournalsForNotAssignedToTruckDriversByCity(inCity);

        Map<Driver, Float> workingHours = sumWorkingHoursForThisMonth(journals);
        
        filterDriversByMaxWorkingHours(workingHours, maxWorkingHours);
        
        return new HashSet<Driver>(0);
    }

    /**
     * Filter Map of working hours records.
     * Delete entry if limit of hours is exceeded.
     * 
     * @param workingHours
     * @param maxWorkingHours
     */
    private void filterDriversByMaxWorkingHours(
            Map<Driver, Float> workingHours, float maxWorkingHours) {
        for (Entry<Driver, Float> e : workingHours.entrySet()) {
            if (e.getValue() >= maxWorkingHours) {
                workingHours.remove(e.getKey());
            }
        }
    }

    /**
     * Calculate total working hours for Drivers that are listed in Shift journal.
     * 
     * @param journal
     * @return Map with Driver as keys and working hours as values.
     */
    public Map<Driver, Float> sumWorkingHoursForThisMonth(
            Collection<DriverShiftJournal> journal) {

        Map<Driver, Float> workingHoursForDrivers = new HashMap<Driver, Float>();
        
        Date firstDayOfCurrentMonth = DateUtils.getFirstDateOfCurrentMonth();
        Date firstDayOfNextMonth = DateUtils.getFirstDayOfNextMonth();
        
        for (DriverShiftJournal j : journal) {
            Driver driver = j.getDriverForThisRecord();
            Date shiftBeggined = j.getShiftBeggined();
            Date shiftEnded = j.getShiftEnded();
            
            /*
             * If shift ended after or before current month
             * we trim it by limiting shift end to first day of next
             * month (00:00:00) and  shift start to first day of month (00:00:00).
             */
            if (shiftBeggined.getTime() < firstDayOfCurrentMonth.getTime()) {
                shiftBeggined = firstDayOfCurrentMonth;
            }
            if (shiftEnded.getTime() > firstDayOfNextMonth.getTime()) {
                shiftEnded = firstDayOfNextMonth;
            }
            
            float shiftDuration = DateUtils.diffInHours(shiftBeggined, shiftEnded);
            
            Float totalHoursForDriver = workingHoursForDrivers.get(driver);
            
            if(totalHoursForDriver == null) {
                workingHoursForDrivers.put(driver, shiftDuration);
            } else {
                workingHoursForDrivers.put(driver, totalHoursForDriver + shiftDuration);
            }
        }
        
        return workingHoursForDrivers;        
    }
    
    
    /**
     * Find shift journal records for drivers for current month.
     * Drivers are searched by <code>city</code> and they must not be assigned to Truck
     * 
     * Journal Record is selected if shift is ended or started in this month.
     * 
     * @param city
     * @return
     */
    public Set<DriverShiftJournal> findThisMonthJournalsForNotAssignedToTruckDriversByCity(
            City city) {
        EntityManager em = getEntityManager();
        String journalEntityName = DriverShiftJournal.class.getSimpleName();
        String driverEntityName = getEntityClass().getSimpleName();

        String queryString = "SELECT j FROM " + journalEntityName + " j"
                + " WHERE shiftEnded IS NOT NULL"
                + " AND driverForThisRecord IN (SELECT id FROM " + driverEntityName
                + " WHERE currentTruck IS NULL AND currentCity = :city)"
                + " AND ( (shiftEnded BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth)"
                + " OR (shiftBeggined BETWEEN :firstDayOfMonth AND :firstDayOfNextMonth) )";
        
        Query query = em.createQuery(queryString, DriverShiftJournal.class);
        query.setParameter("city", city);
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

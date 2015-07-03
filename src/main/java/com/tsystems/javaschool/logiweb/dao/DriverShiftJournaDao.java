package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Driver;
import com.tsystems.javaschool.logiweb.model.DriverShiftJournal;

/**
 * CRUD operations for Driver Shift entity.
 * 
 * @author Andrey Baliushin
 */
public interface DriverShiftJournaDao extends GenericDao<DriverShiftJournal> {

    /**
     * Find shift journal records for current month for drivers.
     * 
     * Record is selected if shift started or ended in this month. 
     * Shifts that are not ended yet are also selected.
     * 
     * @param drivers
     * @return
     */
    Set<DriverShiftJournal> findThisMonthJournalsForDrivers(Set<Driver> drivers);
    
    /**
     * Overload for {@link #findThisMonthJournalsForDrivers(Set)}
     * Puts Driver into set and redirect.
     * 
     * @see #findThisMonthJournalsForDrivers(Set)
     * 
     * @param driver
     * @return
     */
    Set<DriverShiftJournal> findThisMonthJournalsForDrivers(Driver driver);
}

package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.DriverShiftJournal;

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
     * Method does not trim records. If record is started in previous month and ended 
     * in this one, then it will be shown 'as is'. 
     * 
     * @param drivers
     * @return empty set if nothing was found or if empty set of drivers was past as param
     * @throws DaoException if something unexpected happened
     */
    Set<DriverShiftJournal> findThisMonthJournalsForDrivers(Set<Driver> drivers) throws DaoException;
    
    /**
     * Overload for {@link #findThisMonthJournalsForDrivers(Set)}
     * Puts Driver into set and redirect.
     * 
     * @see #findThisMonthJournalsForDrivers(Set)
     * 
     * @param driver
     * @return empty set if nothing was found or if driver is null
     * @throws DaoException if something unexpected happened
     */
    Set<DriverShiftJournal> findThisMonthJournalsForDrivers(Driver driver) throws DaoException;

    DriverShiftJournal findUnfinishedShiftForDriver(Driver driver) throws DaoException;
}


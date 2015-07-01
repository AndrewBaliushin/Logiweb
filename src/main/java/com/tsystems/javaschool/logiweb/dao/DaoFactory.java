package com.tsystems.javaschool.logiweb.dao;

import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;

/**
 * Interface for Dao factory.
 * 
 * @author Andrey Baliushin
 */
public interface DaoFactory {

    CargoDao getCargoDao();

    CityDao getCityDao();

    DeliveryOrderDao getDeliveryOrderDao();

    DriverDao getDriverDao();

    DriverShiftJournaDao geDriverShiftJournaDao();

    TruckDao getTruckDao();
}

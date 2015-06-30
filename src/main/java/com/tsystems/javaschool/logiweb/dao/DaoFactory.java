package com.tsystems.javaschool.logiweb.dao;

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

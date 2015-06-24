package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.model.TruckStatus;

public interface TruckDao extends GenericDao<Truck> {

    Truck getByLicensePlate(String lPlate);

    Set<Truck> getByStatus(TruckStatus status);

    Set<Truck> getByCargoCapacity(Float capacity);

    Set<Truck> getByCurrentLocationCity(City city);

}

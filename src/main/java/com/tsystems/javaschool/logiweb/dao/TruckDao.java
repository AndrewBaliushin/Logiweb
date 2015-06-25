package com.tsystems.javaschool.logiweb.dao;

import java.util.Set;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.Truck;
import com.tsystems.javaschool.logiweb.model.TruckStatus;

public interface TruckDao extends GenericDao<Truck> {


    Set<Truck> getAvailiable(TruckStatus status, float minCargoCapacity);
 
}

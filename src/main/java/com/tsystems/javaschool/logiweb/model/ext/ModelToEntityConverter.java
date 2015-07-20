package com.tsystems.javaschool.logiweb.model.ext;

import java.sql.DriverManager;

import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.model.DriverModel;

public class ModelToEntityConverter {

    private ModelToEntityConverter() {
    }
    
    public static Driver convertToEntity(DriverModel driverModel) {
        Driver entity = new Driver();
        
        City city = new City();
        city.setId(driverModel.getCurrentCityId());
        
        entity.setCurrentCity(city);
        entity.setName(driverModel.getName());
        entity.setSurname(driverModel.getSurname());
        entity.setCurrentTruck(driverModel.getCurrentTruck());
        entity.setEmployeeId(driverModel.getEmployeeId());
        entity.setStatus(driverModel.getStatus());
        
        return entity;
    }

}

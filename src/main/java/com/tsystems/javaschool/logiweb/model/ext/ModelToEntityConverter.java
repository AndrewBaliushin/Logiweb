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
    
    public static DriverModel convertToModel(Driver entity) {
        DriverModel model = new DriverModel();
        
        model.setCurrentCityId(entity.getCurrentCity() == null ? 0 : entity.getCurrentCity().getId());
        
        model.setCurrentTruck(entity.getCurrentTruck());
        model.setEmployeeId(entity.getEmployeeId());
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setOrder(null);
        model.setRouteInfo(null);
        model.setStatus(entity.getStatus());
        model.setSurname(entity.getSurname());
        model.setWorkingHoursThisMonth(0);
        
        return model;
    }
    
    

}

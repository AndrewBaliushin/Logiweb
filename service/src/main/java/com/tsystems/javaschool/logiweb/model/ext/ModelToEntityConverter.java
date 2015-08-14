package com.tsystems.javaschool.logiweb.model.ext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.model.CargoModel;
import com.tsystems.javaschool.logiweb.model.DriverModel;
import com.tsystems.javaschool.logiweb.model.OrderModel;
import com.tsystems.javaschool.logiweb.model.TruckModel;

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
        entity.setEmployeeId(driverModel.getEmployeeId());
        entity.setStatus(driverModel.getStatus());
        
        return entity;
    }
    
    public static DriverModel convertToModel(Driver entity) {
        DriverModel model = new DriverModel();
        
        model.setCurrentCityId(entity.getCurrentCity() == null ? 0 : entity
                .getCurrentCity().getId());

        model.setCurrentTruckLicensePlate(entity.getCurrentTruck() == null ? null
                : entity.getCurrentTruck().getLicencePlate());
        model.setEmployeeId(entity.getEmployeeId());
        model.setId(entity.getId());
        model.setName(entity.getName());
        
        if (model.getCurrentTruckLicensePlate() != null) {
            model.setOrderId(entity.getCurrentTruck()
                    .getAssignedDeliveryOrder().getId());
        }

        if (model.getCurrentTruckLicensePlate() != null
                && entity.getCurrentTruck().getDrivers() != null) {
            Set<Integer> coDriversIds = new HashSet<Integer>();
            Set<Driver> drivers = entity.getCurrentTruck().getDrivers();
            for (Driver driver : drivers) {
                coDriversIds.add(driver.getId());
            }
            model.setCoDriversIds(coDriversIds);
        }

        model.setRouteInfo(null);
        
        model.setStatus(entity.getStatus());
        model.setSurname(entity.getSurname());
        model.setWorkingHoursThisMonth(0);
        
        return model;
    }
    
    public static Set<DriverModel> convertDriversToModels(Set<Driver> entities) {
        Set<DriverModel> models = new HashSet<DriverModel>();
        for (Driver e : entities) {
            models.add(convertToModel(e));
        }
        return models;
    }
    
    public static Truck convertToEntity(TruckModel model) {
        Truck truck = new Truck();
        
        City city = new City();
        city.setId(model.getCurrentCityId());
        
        truck.setId(model.getId());
        truck.setLicencePlate(model.getLicencePlate());
        truck.setCurrentCity(city);
        truck.setCrewSize(model.getCrewSize());
        truck.setCargoCapacity(model.getCargoCapacity());
        truck.setStatus(model.getStatus());
        
        truck.setAssignedDeliveryOrder(null);
        truck.setDrivers(null);
        
        return truck;
    }

    public static TruckModel convertToModel(Truck entity) {
        TruckModel model = new TruckModel();
        
        model.setCurrentCityId(entity.getCurrentCity() == null ? 0 : entity
                .getCurrentCity().getId());
        
        model.setId(entity.getId());
        model.setLicencePlate(entity.getLicencePlate());

        if (entity.getAssignedDeliveryOrder() != null) {
            model.setAssignedDeliveryOrderId(entity.getAssignedDeliveryOrder()
                    .getId());
        }
        
        if (entity.getDrivers() != null) {
            Map<Integer, String> driversIdsAndSurnames = new HashMap<Integer, String>();
            for (Driver d : entity.getDrivers()) {
                driversIdsAndSurnames.put(d.getId(), d.getSurname());
            }
            model.setDriversIdsAndSurnames(driversIdsAndSurnames);
        }
        
        model.setCargoCapacity(entity.getCargoCapacity());
        model.setCrewSize(entity.getCrewSize());
        
        model.setStatus(entity.getStatus());
        
        return model;
    }
    
    public static Set<TruckModel> convertTrucksToModels(Set<Truck> entities) {
        Set<TruckModel> models = new HashSet<TruckModel>();
        for (Truck e : entities) {
            models.add(convertToModel(e));
        }
        return models;
    }
    
    public static OrderModel convertToModel(DeliveryOrder entity) {
        OrderModel model = new OrderModel();
        
        model.setId(entity.getId());
        model.setStatus(entity.getStatus());
        if (entity.getAssignedCargoes() != null) {
            model.setAssignedCargoes(convertCargoesToModels(entity.getAssignedCargoes()));
        }
        if (entity.getAssignedTruck() != null) {
            model.setAssignedTruck(convertToModel(entity.getAssignedTruck()));
        }
        return model;
    }

    public static Set<OrderModel> convertOrdersToModels(
            Set<DeliveryOrder> entities) {
        Set<OrderModel> models = new HashSet<OrderModel>();
        for (DeliveryOrder e : entities) {
            models.add(convertToModel(e));
        }
        return models;
    }
    
    public static CargoModel convertToModel(Cargo entity) {
        CargoModel model = new CargoModel();
        
        model.setId(entity.getId());
        model.setDestinationCityId(entity.getDestinationCity().getId());
        model.setOrderIdForThisCargo(entity.getOrderForThisCargo().getId());
        model.setOriginCityId(entity.getOriginCity().getId());
        model.setStatus(entity.getStatus());
        model.setTitle(entity.getTitle());
        model.setWeight(entity.getWeight());
        
        return model;
    }
    
    public static Cargo convertToEntity(CargoModel model) {
        Cargo entity = new Cargo();
        
        City originCity = new City();
        originCity.setId(model.getOriginCityId());
        
        City destCity = new City();
        destCity.setId(model.getDestinationCityId());
        
        DeliveryOrder orderForCargo = new DeliveryOrder();
        orderForCargo.setId(model.getOrderIdForThisCargo());

        if (model.getId() != null) {
            entity.setId(model.getId());
        }

        entity.setOrderForThisCargo(orderForCargo);
        entity.setOriginCity(originCity);
        entity.setDestinationCity(destCity);
        entity.setStatus(model.getStatus());
        entity.setTitle(model.getTitle());
        entity.setWeight(model.getWeight());
        
        return entity;
    }
    
    public static Set<CargoModel> convertCargoesToModels(Set<Cargo> entities) {
        Set<CargoModel> models = new HashSet<CargoModel>();
        for (Cargo e : entities) {
            models.add(convertToModel(e));
        }
        return models;
    }
}

package com.tsystems.javaschool.logiweb.model;

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.status.TruckStatus;

public class TruckModel {
    
    private Integer id;
    
    @NotBlank
    private String licencePlate;
    
    @NotNull
    @Min(1)
    private int crewSize;
    
    @NotNull
    private int currentCityId;
    
    @NotNull
    private Float cargoCapacity;

    private DeliveryOrder assignedDeliveryOrder;
    private Set<Driver> drivers;
    private TruckStatus status;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getLicencePlate() {
        return licencePlate;
    }
    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }
    public DeliveryOrder getAssignedDeliveryOrder() {
        return assignedDeliveryOrder;
    }
    public void setAssignedDeliveryOrder(DeliveryOrder assignedDeliveryOrder) {
        this.assignedDeliveryOrder = assignedDeliveryOrder;
    }
    public Float getCargoCapacity() {
        return cargoCapacity;
    }
    public void setCargoCapacity(Float cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }
    public int getCrewSize() {
        return crewSize;
    }
    public void setCrewSize(int crewSize) {
        this.crewSize = crewSize;
    }
    public int getCurrentCityId() {
        return currentCityId;
    }
    public void setCurrentCityId(int currentCityId) {
        this.currentCityId = currentCityId;
    }
    public Set<Driver> getDrivers() {
        return drivers;
    }
    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }
    public TruckStatus getStatus() {
        return status;
    }
    public void setStatus(TruckStatus status) {
        this.status = status;
    } 
}

package com.tsystems.javaschool.logiweb.model;

import java.util.Map;
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

    private Integer assignedDeliveryOrderId;
    
    private Map<Integer, String> driversIdsAndSurnames;
    
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
    public TruckStatus getStatus() {
        return status;
    }
    public void setStatus(TruckStatus status) {
        this.status = status;
    }
    public Integer getAssignedDeliveryOrderId() {
        return assignedDeliveryOrderId;
    }
    public void setAssignedDeliveryOrderId(Integer assignedDeliveryOrderId) {
        this.assignedDeliveryOrderId = assignedDeliveryOrderId;
    }
    public Map<Integer, String> getDriversIdsAndSurnames() {
        return driversIdsAndSurnames;
    }
    public void setDriversIdsAndSurnames(Map<Integer, String> driversIdsAndSurnames) {
        this.driversIdsAndSurnames = driversIdsAndSurnames;
    }
}

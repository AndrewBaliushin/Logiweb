package com.tsystems.javaschool.logiweb.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tsystems.javaschool.logiweb.entities.status.CargoStatus;

public class CargoModel {
    
    private Integer id;
    
    @NotNull
    @Size(min = 1, max = 256, message = "Title should be 1-256 chars long.")
    private String title;
    
    @NotNull
    private Float weight;
    
    @NotNull
    private CargoStatus status;
    
    @NotNull
    private int originCityId;
    
    @NotNull
    private int destinationCityId;
    
    @NotNull
    private int orderIdForThisCargo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public CargoStatus getStatus() {
        return status;
    }

    public void setStatus(CargoStatus status) {
        this.status = status;
    }

    public int getOriginCityId() {
        return originCityId;
    }

    public void setOriginCityId(int originCityId) {
        this.originCityId = originCityId;
    }

    public int getDestinationCityId() {
        return destinationCityId;
    }

    public void setDestinationCityId(int destinationCityId) {
        this.destinationCityId = destinationCityId;
    }

    public int getOrderIdForThisCargo() {
        return orderIdForThisCargo;
    }

    public void setOrderIdForThisCargo(int orderIdForThisCargo) {
        this.orderIdForThisCargo = orderIdForThisCargo;
    }

}

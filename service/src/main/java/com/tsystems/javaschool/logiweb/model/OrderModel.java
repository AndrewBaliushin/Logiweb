package com.tsystems.javaschool.logiweb.model;

import java.util.Set;

import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;

public class OrderModel {

    private Integer id;
    
    private OrderStatus status;
    
    private TruckModel assignedTruck;
    
    private Set<CargoModel> assignedCargoes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public TruckModel getAssignedTruck() {
        return assignedTruck;
    }

    public void setAssignedTruck(TruckModel assignedTruck) {
        this.assignedTruck = assignedTruck;
    }

    public Set<CargoModel> getAssignedCargoes() {
        return assignedCargoes;
    }

    public void setAssignedCargoes(Set<CargoModel> assignedCargoes) {
        this.assignedCargoes = assignedCargoes;
    }
    
}

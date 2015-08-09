package com.tsystems.javaschool.logiweb.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tsystems.javaschool.logiweb.entities.status.DriverStatus;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;

public class DriverModel {
    
    private Integer id;

    @NotNull
    @Min(1)
    private Integer employeeId;

    @NotNull
    @Size(min = 1, max = 256, message = "Name should be 1-256 chars long.")
    private String name;

    @NotNull
    @Size(min = 1, max = 256, message = "Surname should be 1-256 chars long.")
    private String surname;

    private DriverStatus status;

    @NotNull
    private int currentCityId;

    private String currentTruckLicensePlate;
    
    private Integer orderId;
    
    private RouteInformation routeInfo;
    
    public RouteInformation getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(RouteInformation routeInfo) {
        this.routeInfo = routeInfo;
    }

    public float getWorkingHoursThisMonth() {
        return workingHoursThisMonth;
    }

    public void setWorkingHoursThisMonth(float workingHoursThisMonth) {
        this.workingHoursThisMonth = workingHoursThisMonth;
    }

    private float workingHoursThisMonth;

    public DriverModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public int getCurrentCityId() {
        return currentCityId;
    }

    public void setCurrentCityId(int currentCityId) {
        this.currentCityId = currentCityId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getCurrentTruckLicensePlate() {
        return currentTruckLicensePlate;
    }

    public void setCurrentTruckLicensePlate(String currentTruckLicensePlate) {
        this.currentTruckLicensePlate = currentTruckLicensePlate;
    }

}

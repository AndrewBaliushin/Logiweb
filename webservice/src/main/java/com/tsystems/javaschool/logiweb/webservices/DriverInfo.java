package com.tsystems.javaschool.logiweb.webservices;

import com.tsystems.javaschool.logiweb.entities.status.DriverStatus;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;

public class DriverInfo {
    
    private int employeeId;

    private String name;
    
    private String surname;
    
    private float workingHoursInThisMonth;
    
    private DriverStatus cutrrentStatus;
    
    private RouteInformation routeInformation;
    
    private OrderStatus assignedOrderStatus;

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

    public float getWorkingHoursInThisMonth() {
        return workingHoursInThisMonth;
    }

    public void setWorkingHoursInThisMonth(float workingHoursInThisMonth) {
        this.workingHoursInThisMonth = workingHoursInThisMonth;
    }

    public DriverStatus getCutrrentStatus() {
        return cutrrentStatus;
    }

    public void setCutrrentStatus(DriverStatus cutrrentStatus) {
        this.cutrrentStatus = cutrrentStatus;
    }

    public RouteInformation getRouteInformation() {
        return routeInformation;
    }

    public void setRouteInformation(RouteInformation routeInformation) {
        this.routeInformation = routeInformation;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public OrderStatus getAssignedOrderStatus() {
        return assignedOrderStatus;
    }

    public void setAssignedOrderStatus(OrderStatus assignedOrderStatus) {
        this.assignedOrderStatus = assignedOrderStatus;
    }
    
    
}

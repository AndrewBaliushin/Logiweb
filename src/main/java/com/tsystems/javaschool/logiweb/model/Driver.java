package com.tsystems.javaschool.logiweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity representation of a Delivery Truck Driver.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue
    @Column(name = "driver_id", unique = true, nullable = false)
    private int id;

    @Column(name = "driver_employee_id", unique = true)
    private int employeeId;

    @Column(name = "driver_name")
    private String name;

    @Column(name = "driver_surname")
    private String surname;

    @Column(name = "driver_working_hours_in_this_month")
    private Float workingHoursInThisMonth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_status_FK")
    private DriverStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_current_location_city_FK")
    private City currentCity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_current_truck_FK")
    private Truck currentTruck;

    @Column(name = "driver_deleted")
    private boolean deletedRecord;

    public Driver() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
	this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
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

    public Float getWorkingHoursInThisMonth() {
        return workingHoursInThisMonth;
    }

    public void setWorkingHoursInThisMonth(Float workingHoursInThisMonth) {
        this.workingHoursInThisMonth = workingHoursInThisMonth;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public Truck getCurrentTruck() {
        return currentTruck;
    }

    public void setCurrentTruck(Truck currentTruck) {
        this.currentTruck = currentTruck;
    }

    public boolean isDeletedRecord() {
        return deletedRecord;
    }

    public void setDeletedRecord(boolean deletedRecord) {
        this.deletedRecord = deletedRecord;
    }
        
}

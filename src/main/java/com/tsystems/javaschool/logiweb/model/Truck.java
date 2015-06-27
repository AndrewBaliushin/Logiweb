package com.tsystems.javaschool.logiweb.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.tsystems.javaschool.logiweb.model.status.TruckStatus;

/**
 * Entity representation of a Delivery Truck.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "trucks", 
       uniqueConstraints = 
       @UniqueConstraint(columnNames = "truck_license_plate_UQ"))
public class Truck {

    @Id
    @GeneratedValue
    @Column(name = "truck_id", unique = true, nullable = false)
    private int id;

    @Column(name = "truck_license_plate_UQ", unique = true)
    private String licencePlate;

    @Column(name = "truck_crew_size", nullable = false)
    private int crewSize;

    @Column(name = "truck_cargo_capacity", nullable = false)
    private Float cargoCapacity;

    @Column(name = "truck_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TruckStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "truck_current_location_city_FK", nullable = false)
    private City currentCity;

    @OneToOne
    @JoinColumn(name = "truck_delivery_order_FK_UQ")
    private DeliveryOrder assignedDeliveryOrder;
    
    @Column(name = "truck_deleted", nullable = false)
    private boolean deletedRecord;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "currentTruck")
    private Set<Driver> drivers;

    public Truck() {
    }

    public int getId() {
	return id;
    }
    
    public void setId(int id) {
	this.id = id;
    }

    public String getLicencePlate() {
	return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
	this.licencePlate = licencePlate;
    }

    public int getCrewSize() {
	return crewSize;
    }

    public void setCrewSize(int crewSize) {
	this.crewSize = crewSize;
    }

    public Float getCargoCapacity() {
	return cargoCapacity;
    }

    public void setCargoCapacity(Float cargoCapacity) {
	this.cargoCapacity = cargoCapacity;
    }

    public TruckStatus getStatus() {
	return status;
    }

    public void setStatus(TruckStatus status) {
	this.status = status;
    }

    public DeliveryOrder getAssignedDeliveryOrder() {
        return assignedDeliveryOrder;
    }

    public void setAssignedDeliveryOrder(DeliveryOrder assignedDeliveryOrder) {
        this.assignedDeliveryOrder = assignedDeliveryOrder;
    }

    public City getCurrentCity() {
	return currentCity;
    }

    public void setCurrentCity(City currentCity) {
	this.currentCity = currentCity;
    }

    public boolean isDeletedRecord() {
	return deletedRecord;
    }

    public void setDeletedRecord(boolean deleted) {
	this.deletedRecord = deleted;
    }

    public Set<Driver> getDrivers() {
	return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
	this.drivers = drivers;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id in db: " + id + "\n");
        sb.append("\n");
        sb.append("licensePlate: " + licencePlate + "\n");
        sb.append("crew size: " + crewSize + "\n");
        sb.append("capacity: " + cargoCapacity + "\n");
        sb.append("status: " + status + "\n");
        sb.append("current city: " + currentCity.getName() + "\n");
        sb.append("delivery order" + assignedDeliveryOrder.getId() + "\n");
        return "hi";
    }

}

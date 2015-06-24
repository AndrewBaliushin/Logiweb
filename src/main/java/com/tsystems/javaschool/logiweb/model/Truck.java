package com.tsystems.javaschool.logiweb.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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

    @Column(name = "truck_crew_size")
    private int crewSize;

    @Column(name = "truck_cargo_capacity")
    private Float cargoCapacity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "truck_status_FK")
    private TruckStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "truck_current_location_city_FK")
    private City currentCity;

    @Column(name = "truck_deleted")
    private boolean deletedRecord;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "assignedTruck")
    private Set<DeliveryOrder> deliveryOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "currentTruck")
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

    public void setCrewSize(Byte crewSize) {
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

    public Set<DeliveryOrder> getDeliveryOrders() {
	return deliveryOrders;
    }

    public void setDeliveryOrders(Set<DeliveryOrder> deliveryOrders) {
	this.deliveryOrders = deliveryOrders;
    }

    public Set<Driver> getDrivers() {
	return drivers;
    }

    public void setDrivers(Set<Driver> drivers) {
	this.drivers = drivers;
    }

}

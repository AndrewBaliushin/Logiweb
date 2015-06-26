package com.tsystems.javaschool.logiweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.tsystems.javaschool.logiweb.model.status.CargoStatus;

/**
 * Entity representation of Cargo.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "cargoes")
public class Cargo {
    
    @Id
    @GeneratedValue
    @Column(name = "cargo_id", unique = true, nullable = false)
    private int id;

    @Column(name = "cargo_title", nullable = false)
    private String title;
    
    @Column(name = "cargo_weight", nullable = false)
    private Float weight;
    
    @Column(name = "cargo_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CargoStatus status;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_origin_city_FK", nullable = false)
    private City originCity;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_destination_city_FK", nullable = false)
    private City destinationCity;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_from_order_FK", nullable = false)
    private DeliveryOrder orderForThisCargo;
    
    public Cargo() {
    }
    
    public void setId(int id) {
	this.id = id;
    }

    public int getId() {
        return id;
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

    public DeliveryOrder getOrderForThisCargo() {
        return orderForThisCargo;
    }

    public void setOrderForThisCargo(DeliveryOrder orderForThisCargo) {
        this.orderForThisCargo = orderForThisCargo;
    }

    public City getOriginCity() {
        return originCity;
    }

    public void setOriginCity(City originCity) {
        this.originCity = originCity;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(City destinationCity) {
        this.destinationCity = destinationCity;
    }
    
}

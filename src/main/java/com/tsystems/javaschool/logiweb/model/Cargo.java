package com.tsystems.javaschool.logiweb.model;

import java.util.HashSet;
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

    @Column(name = "cargo_title")
    private String title;
    
    @Column(name = "cargo_weight")
    private Float weight;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_status_FK")
    private CargoStatus status;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cargoFromOrder")
    private Set<DeliveryOrderWaypoint> orderWaypointsForThisCargo;

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

    public Set<DeliveryOrderWaypoint> getOrderWaypointsForThisCargo() {
        return orderWaypointsForThisCargo;
    }

    public void setOrderWaypointsForThisCargo(
            Set<DeliveryOrderWaypoint> orderWaypointsForThisCargo) {
        this.orderWaypointsForThisCargo = orderWaypointsForThisCargo;
    }
    
}

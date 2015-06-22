package com.tsystems.javaschool.logiweb.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity representation of Operation type for Cargo.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "cargo_operation_types")
public class CargoOperationType {
    
    @Id
    @GeneratedValue
    @Column(name = "cargo_operation_id", unique = true, nullable = false)
    private int id;
    
    @Column(name = "cargo_operation_discription")
    private String discription;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operationWithCargo")
    private Set<DeliveryOrderWaypoint> allWaypointsForThisOperationType;

    public CargoOperationType() {
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Set<DeliveryOrderWaypoint> getAllWaypointsForThisOperationType() {
        return allWaypointsForThisOperationType;
    }

    public void setAllWaypointsForThisOperationType(
            Set<DeliveryOrderWaypoint> allWaypointsForThisOperationType) {
        this.allWaypointsForThisOperationType = allWaypointsForThisOperationType;
    }
}

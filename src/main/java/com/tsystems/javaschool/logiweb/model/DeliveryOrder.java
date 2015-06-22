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

/**
 * Entity representation of a Delivery Order for some Cargo.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "delivery_orders")
public class DeliveryOrder {

    @Id
    @GeneratedValue
    @Column(name = "order_id", unique = true, nullable = false)
    private int id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_assigned_truck_FK")
    private Truck assignedTruck;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_status_FK")
    private OrderStatus status;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cargoFromOrder")
    private Set<DeliveryOrderWaypoint> deliveryWaypoints;

    public DeliveryOrder() {
    }
    
    public int getId() {
        return id;
    }

    public Truck getAssignedTruck() {
        return assignedTruck;
    }

    public void setAssignedTruck(Truck assignedTruck) {
        this.assignedTruck = assignedTruck;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Set<DeliveryOrderWaypoint> getDeliveryWaypoints() {
        return deliveryWaypoints;
    }

    public void setDeliveryWaypoints(Set<DeliveryOrderWaypoint> deliveryWaypoints) {
        this.deliveryWaypoints = deliveryWaypoints;
    }
    
}

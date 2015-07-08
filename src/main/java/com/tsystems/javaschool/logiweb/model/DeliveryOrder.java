package com.tsystems.javaschool.logiweb.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tsystems.javaschool.logiweb.model.status.OrderStatus;

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
    
    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToOne(mappedBy = "assignedDeliveryOrder")
    private Truck assignedTruck;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "orderForThisCargo")
    private Set<Cargo> assignedCargoes;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public DeliveryOrder() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
	this.id = id;
    }

    public Truck getAssignedTruck() {
        return assignedTruck;
    }

    public void setAssignedTruck(Truck assignedTruck) {
	assignedTruck.setAssignedDeliveryOrder(this);
        this.assignedTruck = assignedTruck;
    }

    public Set<Cargo> getAssignedCargoes() {
        return assignedCargoes;
    }

    public void setAssignedCargoes(Set<Cargo> assignedCargoes) {
        for (Cargo cargo : assignedCargoes) {
            cargo.setOrderForThisCargo(this);
        }
        this.assignedCargoes = assignedCargoes;
    }
}

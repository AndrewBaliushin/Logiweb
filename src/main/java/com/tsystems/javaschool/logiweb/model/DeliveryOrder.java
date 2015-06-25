package com.tsystems.javaschool.logiweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.tsystems.javaschool.logiweb.model.status.CargoStatus;
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
    
    @OneToOne(mappedBy = "assignedDeliveryOrder")
    private Truck assignedTruck;

    @Column(name = "order_status")
    private int status;
    
    public OrderStatus getStatus() {
        return OrderStatus.getById(status);
    }

    public void setStatus(OrderStatus status) {
        this.status = status.getIdInDb();
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
    
}

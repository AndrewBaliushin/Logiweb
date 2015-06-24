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
 * Entity representation for Status of the Order.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "order_statuses")
public class OrderStatus {

    @Id
    @GeneratedValue
    @Column(name = "order_status_id", unique = true, nullable = false)
    private int id;
    
    @Column(name = "order_status_description")
    private String discription;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<DeliveryOrder> ordersWithThisStatus;

    public OrderStatus() {
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

    public Set<DeliveryOrder> getOrdersWithThisStatus() {
        return ordersWithThisStatus;
    }

    public void setOrdersWithThisStatus(Set<DeliveryOrder> ordersWithThisStatus) {
        this.ordersWithThisStatus = ordersWithThisStatus;
    }

}

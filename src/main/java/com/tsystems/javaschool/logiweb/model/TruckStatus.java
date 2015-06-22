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
 * Entity representation for Status of the Truck.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "truck_statuses")
public class TruckStatus {

    @Id
    @GeneratedValue
    @Column(name = "truck_status_id", unique = true, nullable = false)
    private int id;
    
    @Column(name = "truck_status_discription")
    private String discription;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Truck> trucksWithThisStatus;

    public int getId() {
        return id;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Set<Truck> getTrucksWithThisStatus() {
        return trucksWithThisStatus;
    }

    public void setTrucksWithThisStatus(Set<Truck> trucksWithThisStatus) {
        this.trucksWithThisStatus = trucksWithThisStatus;
    }
    
}

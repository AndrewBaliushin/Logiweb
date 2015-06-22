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
 * Entity representation for Status of the Driver.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "driver_statuses")
public class DriverStatus {

    @Id
    @GeneratedValue
    @Column(name = "driver_status_id", unique = true, nullable = false)
    private int id;
    
    @Column(name = "driver_status_discription")
    private String discription;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Driver> driversWithThisStatus;

    public DriverStatus() {
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

    public Set<Driver> getDriversWithThisStatus() {
        return driversWithThisStatus;
    }

    public void setDriversWithThisStatus(Set<Driver> driversWithThisStatus) {
        this.driversWithThisStatus = driversWithThisStatus;
    }

}

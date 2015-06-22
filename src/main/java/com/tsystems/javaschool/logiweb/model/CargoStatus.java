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
 * Entity representation for Status of the Cargo.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "cargo_statuses")
public class CargoStatus {

    @Id
    @GeneratedValue
    @Column(name = "cargo_status_id", unique = true, nullable = false)
    private int id;
    
    @Column(name = "cargo_status_discription")
    private String discription;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    private Set<Cargo> cargoesWithThisStatus;
    
    public CargoStatus() {
    }

    public int getId() {
        return id;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Set<Cargo> getCargoesWithThisStatus() {
        return cargoesWithThisStatus;
    }

    public void setCargoesWithThisStatus(Set<Cargo> cargoesWithThisStatus) {
        this.cargoesWithThisStatus = cargoesWithThisStatus;
    }

}

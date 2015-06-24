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
 * Entity representation of a City.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "cities")
public class City {
    
    @Id
    @GeneratedValue
    @Column(name = "city_id", unique = true, nullable = false)
    private int id;
    
    @Column(name = "city_name")
    private String name;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "currentCity")
    private Set<Truck> trucksInCity;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "currentCity")
    private Set<Driver> driversInCity;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "originCity")
    private Set<Cargo> originCityForCargoes;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "destinationCity")
    private Set<Cargo> destinationCityForCargoes;

    public City() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
	this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Truck> getTrucksInCity() {
        return trucksInCity;
    }

    public void setTrucksInCity(Set<Truck> trucksInCity) {
        this.trucksInCity = trucksInCity;
    }

    public Set<Driver> getDriversInCity() {
        return driversInCity;
    }

    public void setDriversInCity(Set<Driver> driversInCity) {
        this.driversInCity = driversInCity;
    }

    public Set<Cargo> getOriginCityForCargoes() {
        return originCityForCargoes;
    }

    public void setOriginCityForCargoes(Set<Cargo> originCityForCargoes) {
        this.originCityForCargoes = originCityForCargoes;
    }

    public Set<Cargo> getDestinationCityForCargoes() {
        return destinationCityForCargoes;
    }

    public void setDestinationCityForCargoes(Set<Cargo> destinationCityForCargoes) {
        this.destinationCityForCargoes = destinationCityForCargoes;
    }
    
}

package com.tsystems.javaschool.logiweb.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    @Column(name = "city_name", nullable = false)
    private String name;

    public City() {
    }

    public City(int id) {
        setId(id);
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
}

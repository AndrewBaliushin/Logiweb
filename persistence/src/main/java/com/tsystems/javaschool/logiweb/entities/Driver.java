package com.tsystems.javaschool.logiweb.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.tsystems.javaschool.logiweb.entities.status.DriverStatus;

/**
 * Entity representation of a Delivery Truck Driver.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "drivers", uniqueConstraints = @UniqueConstraint(columnNames = "driver_employee_id_UQ"))
public class Driver {

    @Id
    @GeneratedValue
    @Column(name = "driver_id", unique = true, nullable = false)
    private int id;

    @Column(name = "driver_employee_id_UQ", unique = true, nullable = false)
    private int employeeId;

    @OneToOne
    @JoinColumn(name = "driver_logiweb_account_id")
    private LogiwebUser logiwebAccount;

    @NotNull
    @Column(name = "driver_name", nullable = false)
    private String name;

    @Column(name = "driver_surname", nullable = false)
    private String surname;

    @Column(name = "driver_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DriverStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_current_location_city_FK", nullable = false)
    private City currentCity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_current_truck_FK")
    private Truck currentTruck;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driverForThisRecord")
    private Set<DriverShiftJournal> shitsJournalRecords;

    @Column(name = "driver_deleted", nullable = false)
    private boolean deletedRecord;

    public Driver() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(City currentCity) {
        this.currentCity = currentCity;
    }

    public Truck getCurrentTruck() {
        return currentTruck;
    }

    public void setCurrentTruck(Truck currentTruck) {
        this.currentTruck = currentTruck;
    }

    public Set<DriverShiftJournal> getShitsJournalRecords() {
        return shitsJournalRecords;
    }

    public void setShitsJournalRecords(
            Set<DriverShiftJournal> shitsJournalRecords) {
        this.shitsJournalRecords = shitsJournalRecords;
    }

    public boolean isDeletedRecord() {
        return deletedRecord;
    }

    public void setDeletedRecord(boolean deletedRecord) {
        this.deletedRecord = deletedRecord;
    }

    public LogiwebUser getLogiwebAccount() {
        return logiwebAccount;
    }

    public void setLogiwebAccount(LogiwebUser logiwebAccount) {
        this.logiwebAccount = logiwebAccount;
    }

}

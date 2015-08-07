package com.tsystems.javaschool.logiweb.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the Driver Shifts (working hours).
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "drivers_shift_journal")
public class DriverShiftJournal {

    @Id
    @GeneratedValue
    @Column(name = "driver_shift_record_id", unique = true, nullable = false)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "driver_shift_beggined", nullable = false)
    private Date shiftBeggined;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "driver_shift_ended")
    private Date shiftEnded;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shift_record_for_driver_FK", nullable = false)
    private Driver driverForThisRecord;

    public DriverShiftJournal() {
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public Date getShiftBeggined() {
	return shiftBeggined;
    }

    public void setShiftBeggined(Date shiftBeggined) {
	this.shiftBeggined = shiftBeggined;
    }

    public Date getShiftEnded() {
	return shiftEnded;
    }

    public void setShiftEnded(Date shiftEnded) {
	this.shiftEnded = shiftEnded;
    }

    public Driver getDriverForThisRecord() {
	return driverForThisRecord;
    }

    public void setDriverForThisRecord(Driver driverForThisRecord) {
	this.driverForThisRecord = driverForThisRecord;
    }

}
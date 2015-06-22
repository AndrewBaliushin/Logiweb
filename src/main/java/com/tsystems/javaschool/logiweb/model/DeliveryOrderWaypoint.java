package com.tsystems.javaschool.logiweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity representation of Waypoint for delivering or picking up Cargo.
 * 
 * @author Andrey Baliushin
 */
@Entity
@Table(name = "delivery_orders_waypoints")
public class DeliveryOrderWaypoint {

    @Id
    @GeneratedValue
    @Column(name = "waypoint_id", unique = true, nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "waypoint_for_cargo_FK")
    private Cargo waypointForCargo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "waypoint_destination_city_FK")
    private City destinationCity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_operation_type_FK")
    private CargoOperationType operationWithCargo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_from_order_FK")
    private DeliveryOrder cargoFromOrder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cargo getWaypointForCargo() {
        return waypointForCargo;
    }

    public void setWaypointForCargo(Cargo waypointForCargo) {
        this.waypointForCargo = waypointForCargo;
    }

    public City getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(City destinationCity) {
        this.destinationCity = destinationCity;
    }

    public CargoOperationType getOperationWithCargo() {
        return operationWithCargo;
    }

    public void setOperationWithCargo(CargoOperationType operationWithCargo) {
        this.operationWithCargo = operationWithCargo;
    }

    public DeliveryOrder getCargoFromOrder() {
        return cargoFromOrder;
    }

    public void setCargoFromOrder(DeliveryOrder cargoFromOrder) {
        this.cargoFromOrder = cargoFromOrder;
    }
    
}

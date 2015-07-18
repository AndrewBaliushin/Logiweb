package com.tsystems.javaschool.logiweb.service.ext;

import java.util.List;
import java.util.Map;

import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.City;

/**
 * Date holder for route information: best order of Cities to deliver,
 * maximum weight during delivery and estimated time to finish delivery.
 * 
 * @author Andrey Baliushin
 */
public class RouteInformation {
    
    /**
     * Enum of possible operations with cargo.
     * 
     * @author Andrey Baliushin
     */
    public enum OperationWithCargo {
        PICKUP,
        DELIVER;
    }
    
    /**
     * Data container that have City, cargo and operation with cargo in it.
     * @author Andrey Baliushin
     */
    public static class Waypoint {
        
        private OperationWithCargo operation;
        private City city;
        private Cargo cargo;
        
        public Waypoint(OperationWithCargo operation, City city, Cargo cargo) {
            this.operation = operation;
            this.city = city;
            this.cargo = cargo;
        }

        public OperationWithCargo getOperation() {
            return operation;
        }

        public City getCity() {
            return city;
        }
        
        public Cargo getCargo() {
            return cargo;
        }        
    }
    
    private float estimatedTime;
    private float maxWeightOnCourse;
    private List<Waypoint> bestOrderOfDelivery;
    
    public RouteInformation(float estimatedTime, float maxWeightOnCourse,
            List<Waypoint> bestOrderOfDelivery) {
        super();
        this.estimatedTime = estimatedTime;
        this.maxWeightOnCourse = maxWeightOnCourse;
        this.bestOrderOfDelivery = bestOrderOfDelivery;
    }

    /**
     * Get estimated time to deliver order.
     * @return
     */
    public float getEstimatedTime() {
        return estimatedTime;
    }
    
    /**
     * Max cargo weight during delivery.
     * @return
     */
    public float getMaxWeightOnCourse() {
        return maxWeightOnCourse;
    }
    
    /**
     * City list in order that is optimal and most efficient in respect of
     * delivery.
     * @return
     */
    public List<Waypoint> getBestOrderOfDelivery() {
        return bestOrderOfDelivery;
    }
}

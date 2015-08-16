package com.tsystems.javaschool.logiweb.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.status.CargoStatus;
import com.tsystems.javaschool.logiweb.service.RouteService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation.OperationWithCargo;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation.Waypoint;

/**
 * Stub! Does not provide actual information.  
 * 
 * @author Andrey Baliushin
 *
 */
@Service
public class RouteServiceStub implements RouteService {

    /**
     * Used for generation of random delivery time.
     * Each cargo in order adds some time to total delivery time.
     * This sets minimal limit for each cargo.
     */
    private static final float MIN_DELIVERY_TIME = 10;
    
    /**
     * Used for generation of random delivery time.
     * Each cargo in order adds some time to total delivery time.
     * This sets max limit for each cargo.
     */
    private static final float MAX_DELIVERY_TIME = 20;

    private DeliveryOrderDao deliveryOrderDao;
    
    @Autowired
    public RouteServiceStub(DeliveryOrderDao deliveryOrderDao) {
        super();
        this.deliveryOrderDao = deliveryOrderDao;
    }
    
    
    /**
     * Return RouteInformation object, that have random delivery hours (from 10 to 100);
     * order of delivery is sorted by origin first, then destination;
     * weight is maximum weight (all cargo combined).
     * @throws LogiwebServiceException 
     */
    @Override
    public RouteInformation getRouteInformationForOrder(int orderId) throws LogiwebServiceException {
        try {
            DeliveryOrder order = deliveryOrderDao.find(orderId);
            if (order == null) {
                return null;
            }
            
            return new RouteInformation(
                    getPseudoRandomFloatBasedOnCargoesInOrder(order),
                    getTotalWeightOfAllCargoes(order),
                    getCitiesInOrderOriginBeforeDestination(order));
        } catch (DaoException e) {
            throw new LogiwebServiceException("Unexpected exception.");
        }
        
    }

    /**
     * Make ordered list of waypoints where origin cities for all cargo are first,
     * then all destination city.
     * Waypoints in each category (deliver and pickup) are ordered.
     * @param order
     * @return
     */
    private List<Waypoint> getCitiesInOrderOriginBeforeDestination(
            DeliveryOrder order) {
        List<Waypoint> originWaypoints = new ArrayList<Waypoint>();
        List<Waypoint> destinationWaypoints = new ArrayList<Waypoint>();

        Set<Cargo> cargoes = order.getAssignedCargoes();
        if (cargoes == null) {
            return new ArrayList<Waypoint>(0);
        }
        for (Cargo cargo : cargoes) {
            originWaypoints.add(new Waypoint(OperationWithCargo.PICKUP, cargo
                    .getOriginCity(), cargo));
            destinationWaypoints.add(new Waypoint(OperationWithCargo.DELIVER, cargo
                    .getDestinationCity(), cargo));
        }
        
        /* Anonymous comparator for waypoints. Sort by city id.*/
        Comparator<Waypoint> wpCompareByCityId = new Comparator<Waypoint>() {
            @Override
            public int compare(Waypoint w1, Waypoint w2) {
                return w1.getCity().getId() - w2.getCity().getId();
            }
        };

        Collections.sort(originWaypoints, wpCompareByCityId);
        Collections.sort(destinationWaypoints, wpCompareByCityId);

        List<Waypoint> allWaypoints = new ArrayList<Waypoint>(originWaypoints);
        allWaypoints.addAll(destinationWaypoints);
        return allWaypoints;
    }

    private float getTotalWeightOfAllCargoes(DeliveryOrder order) {
        float totalWeight = 0;

        Set<Cargo> cargoes = order.getAssignedCargoes();
        if (cargoes == null) {
            return 0;
        }
        for (Cargo cargo : cargoes) {
            totalWeight += cargo.getWeight();
        }
        return totalWeight;
    }

    private float getPseudoRandomFloatBasedOnCargoesInOrder(DeliveryOrder order) {
        Set<Cargo> cargoes = order.getAssignedCargoes();
        if (cargoes == null) {
            return 0f;
        }
        
        float result = 0f;
        
        Random rand = new Random();
        for (Cargo cargo : cargoes) {
            if (cargo.getStatus() == CargoStatus.DELIVERED) {
                continue;
            }
            
            rand.setSeed(cargo.getDestinationCity().getName().hashCode());
            result += rand.nextFloat() * (MAX_DELIVERY_TIME - MIN_DELIVERY_TIME) + MIN_DELIVERY_TIME;
        }
        return result;
    }

}

package com.tsystems.javaschool.logiweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.tsystems.javaschool.logiweb.model.Cargo;
import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;
import com.tsystems.javaschool.logiweb.service.RouteService;

/**
 * Stub! Does not provide actual information.  
 * 
 * @author Andrey Baliushin
 *
 */
public class RouteServiceStub implements RouteService {

    /**
     * Return RouteInformation object, that have random delivery hours (from 10 to 100);
     * order of delivery is sorted by origin first, then destination;
     * weight is maximum weight (all cargo combined).
     */
    @Override
    public RouteInformation getRouteInformationForOrder(DeliveryOrder order) {
        RouteInformation info = new RouteInformation(
                getRandomFloat(10, 100),
                getTotalWeightOfAllCargoes(order),
                getCitiesInOrderOriginBeforeDestination(order));
        return info;
    }

    private List<City> getCitiesInOrderOriginBeforeDestination(
            DeliveryOrder order) {
        List<City> originCities = new ArrayList<City>();
        List<City> destinationCities = new ArrayList<City>();

        Set<Cargo> cargoes = order.getAssignedCargoes();
        if (cargoes == null) return originCities;
        for (Cargo cargo : cargoes) {
            originCities.add(cargo.getOriginCity());
            destinationCities.add(cargo.getDestinationCity());
        }

        originCities.addAll(destinationCities);
        return originCities;
    }

    private float getTotalWeightOfAllCargoes(DeliveryOrder order) {
        float totalWeight = 0;

        Set<Cargo> cargoes = order.getAssignedCargoes();
        if (cargoes == null) return 0;
        for (Cargo cargo : cargoes) {
            totalWeight += cargo.getWeight();
        }
        return totalWeight;
    }

    private float getRandomFloat(float min, float max) {
        Random rand = new Random();
        float randomFloat = rand.nextFloat() * (max - min) + min;
        return randomFloat;
    }

}

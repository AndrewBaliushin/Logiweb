package com.tsystems.javaschool.logiweb.service;

import java.util.List;

import com.tsystems.javaschool.logiweb.model.City;
import com.tsystems.javaschool.logiweb.model.DeliveryOrder;

/**
 * Service for analysis of Delivery Order. 
 * Helps in creation of optimal delivery routes and estimation of delivery time.
 * 
 * @author Andrey Baliushin
 */
public interface RouteService {
    
    /**
     * Date holder for route information: best order of Cities to deliver,
     * maximum weight during delivery and estimated time to finish delivery.
     * 
     * @author Andrey Baliushin
     */
    class RouteInformation {
        
        private float estimatedTime;
        private float maxWeightOnCourse;
        private List<City> bestOrderOfDelivery;
        
        public RouteInformation(float estimatedTime, float maxWeightOnCourse,
                List<City> bestOrderOfDelivery) {
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
        public List<City> getBestOrderOfDelivery() {
            return bestOrderOfDelivery;
        }
    }

    /**
     * Analyze order and return object with rout data.
     * 
     * @param order
     * @return {@link RouteInformation}
     */
    RouteInformation getRouteInformationForOrder(DeliveryOrder order);
}

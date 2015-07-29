package com.tsystems.javaschool.logiweb.webservices;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface DriverWebService {

    void shiftBegginedForDriver(
            @WebParam(name = "DriverEmployeeId") int driverEmployeeId,
            @WebParam(name = "IsBehindWheel") boolean isBehindWheel);

    void setStatusRestingForDriver(
            @WebParam(name = "DriverEmployeeId") int driverEmployeeId);

    void setStatusDrivingForDriver(
            @WebParam(name = "DriverEmployeeId") int driverEmployeeId);

    void setStatusPickedUpForCargo(@WebParam(name = "CargoId") int cargoId);

    /**
     * Sets 'delivered' status for cargo and check if status of order should be set to 
     * 'delivered' (if all cargoes were delivered)
     * @param cargoId
     */
    void setStatusDeliveredForCargoAndFinilizeOrderIfPossible(@WebParam(name = "CargoId") int cargoId);

    String getDriverInfo(
            @WebParam(name = "DriverEmployeeId") int driverEmployeeId);
}

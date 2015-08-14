package com.tsystems.javaschool.logiweb.webservices;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.tsystems.javaschool.logiweb.webservices.exceptions.InvalidRequestException;
import com.tsystems.javaschool.logiweb.webservices.exceptions.NotFoundException;

@WebService
public interface WsDriver {

    /**
     * Start new shift for driver.
     * 
     * @param driverEmployeeId
     * @throws InvalidRequestException if unfinished shift for this driver is exist. Or if driver
     *             does not exist. Or if driver status is not FREE.
     */
    void shiftBegginedForDriver(
            @WebParam(name = "DriverEmployeeId") int driverEmployeeId)
            throws InvalidRequestException;

    /**
     * End shift for driver.
     * 
     * @param driverEmployeeId
     * @throws InvalidRequestException if there is no unfinished shift for this driver. Or if driver
     *             does not exist.
     */
    void shiftEndedForDriver(
            @WebParam(name = "DriverEmployeeId") int driverEmployeeId)
            throws InvalidRequestException;

    void setStatusRestingForDriver(
            @WebParam(name = "DriverEmployeeId") int driverEmployeeId);

    void setStatusDrivingForDriver(
            @WebParam(name = "DriverEmployeeId") int driverEmployeeId);

    /**
     * Set 'Picked up' status for cargo
     * 
     * @param cargoId
     * @throws IllegalStateException
     *             if cargo is not in 'Ready for pickup' state or order is not in
     *             'Ready to go' state
     * @throws NotFoundException if cargo not exist
     */
    void setStatusPickedUpForCargo(@WebParam(name = "CargoId") int cargoId)
            throws IllegalStateException, NotFoundException;

    /**
     * Set 'delivered' status for cargo and check if status of order should be
     * set to 'delivered' (if all cargoes were delivered)
     * 
     * @param cargoId
     * @throws IllegalStateException
     *             if cargo is not in 'Picked Up' state or order is not in
     *             'Ready to go' state
     * @throws NotFoundException if cargo not exist
     */
    void setStatusDeliveredForCargoAndFinilizeOrderIfPossible(
            @WebParam(name = "CargoId") int cargoId)
            throws IllegalStateException, NotFoundException;

    DriverInfo getDriverInfo(
            @WebParam(name = "DriverEmployeeId") int driverEmployeeId)
            throws NotFoundException;
}

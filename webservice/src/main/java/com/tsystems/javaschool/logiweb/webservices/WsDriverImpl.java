package com.tsystems.javaschool.logiweb.webservices;

import javax.jws.WebService;
import javax.ws.rs.ServerErrorException;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.service.CargoService;
import com.tsystems.javaschool.logiweb.service.DriverService;
import com.tsystems.javaschool.logiweb.service.OrderService;
import com.tsystems.javaschool.logiweb.service.RouteService;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.RecordNotFoundServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.service.ext.RouteInformation;
import com.tsystems.javaschool.logiweb.webservices.exceptions.InvalidRequestException;
import com.tsystems.javaschool.logiweb.webservices.exceptions.NotFoundException;

@Service
@WebService(endpointInterface = "com.tsystems.javaschool.logiweb.webservices.WsDriver")
public class WsDriverImpl implements WsDriver {

    private final static Logger LOG = Logger.getLogger(WsDriverImpl.class);

    @Autowired
    private DriverService driverService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private CargoService cargoService;
    @Autowired
    private OrderService ordersAndCargoService;
    @Autowired
    private TrucksService truckService;

    @Override
    public void shiftBegginedForDriver(int driverEmployeeId)
            throws InvalidRequestException {
        try {
            driverService.startShiftForDriver(driverEmployeeId);
        } catch (ServiceValidationException e) {
            throw new InvalidRequestException(e);
        } catch (LogiwebServiceException e) {
            LOG.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }

    }

    @Override
    public void shiftEndedForDriver(int driverEmployeeId)
            throws InvalidRequestException {
        try {
            driverService.endShiftForDriver(driverEmployeeId);
        } catch (ServiceValidationException e) {
            throw new InvalidRequestException(e);
        } catch (LogiwebServiceException e) {
            LOG.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void setStatusRestingForDriver(int driverEmployeeId) {
        try {
            driverService.setDriverStatusToResting(driverEmployeeId);
        } catch (RecordNotFoundServiceException e) {
            throw new NotFoundException();
        } catch (LogiwebServiceException e) {
            LOG.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void setStatusDrivingForDriver(int driverEmployeeId) {
        try {
            driverService.setDriverStatusToDriving(driverEmployeeId);
        } catch (RecordNotFoundServiceException e) {
            throw new NotFoundException();
        } catch (LogiwebServiceException e) {
            LOG.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public void setStatusPickedUpForCargo(int cargoId) {
        try {
            cargoService.setPickedUpStatus(cargoId);
        } catch (RecordNotFoundServiceException e) {
            throw new NotFoundException();
        } catch (LogiwebServiceException e) {
            LOG.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatusDeliveredForCargoAndFinilizeOrderIfPossible(int cargoId) {
        try {
            cargoService.setDeliveredStatus(cargoId);

            DeliveryOrder order = cargoService.findById(cargoId)
                    .getOrderForThisCargo();
            Truck assignedToTruck = order.getAssignedTruck();
            int orderId = order.getId();
            if (ordersAndCargoService.isAllCargoesInOrderDelivered(orderId)) {
                ordersAndCargoService.setStatusDeliveredForOrder(orderId);
                truckService
                        .removeAssignedOrderAndDriversFromTruck(assignedToTruck
                                .getId());
            }
        } catch (RecordNotFoundServiceException e) {
            throw new NotFoundException();
        } catch (LogiwebServiceException e) {
            LOG.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

    @Override
    public DriverInfo getDriverInfo(int driverEmployeeId)
            throws NotFoundException {
        try {
            Driver driver = driverService
                    .findDriverByEmployeeId(driverEmployeeId);

            if (driver == null) {
                throw new NotFoundException("Driver employee id#"
                        + driverEmployeeId + "not found.");
            }

            DriverInfo info = new DriverInfo();

            info.setEmployeeId(driver.getEmployeeId());
            info.setCutrrentStatus(driver.getStatus());
            info.setName(driver.getName());
            info.setSurname(driver.getSurname());
            info.setWorkingHoursInThisMonth(driverService
                    .calculateWorkingHoursForDriver(driver.getId()));

            if (driver.getCurrentTruck() != null
                    && driver.getCurrentTruck().getAssignedDeliveryOrder() != null) {
                RouteInformation routeInfo = routeService
                        .getRouteInformationForOrder(driver.getCurrentTruck()
                                .getAssignedDeliveryOrder());
                info.setRouteInformation(routeInfo);
            }

            return info;
        } catch (LogiwebServiceException e) {
            LOG.warn("Something unexpected happen", e);
            throw new ServerErrorException(500);
        }
    }

}

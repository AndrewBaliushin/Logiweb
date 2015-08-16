package com.tsystems.javaschool.logiweb.service.impl;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;
import org.mockito.Mockito;

import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.model.TruckModel;
import com.tsystems.javaschool.logiweb.service.TrucksService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;
import com.tsystems.javaschool.logiweb.service.validators.LicensePlateValidator;

public class TrucksSeviceimplTest {
    
    private TruckDao truckDaoMock;
    private LicensePlateValidator plateValidator;
    
    private void mockSetup() {
        truckDaoMock = mock(TruckDao.class);
        plateValidator = mock(LicensePlateValidator.class);
        when(plateValidator.validateLicensePlate(any(String.class)))
                .thenReturn(true);
    }

    /**
     * Test: editTruck
     * Case: id not set in model
     */
    @Test(expected = ServiceValidationException.class)
    public void testEditTruckWhenIdNotSet() throws ServiceValidationException,
            LogiwebServiceException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        TruckModel tm = new TruckModel();
        tm.setId(null);
        truckService.editTruck(tm);
    }
    
    /**
     * Test: editTruck
     * Case: license plate is occupied by another truck
     */
    @Test(expected = ServiceValidationException.class)
    public void testEditTruckWhenLicensePlateOccupied()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        //needed to not fail other checks and validations
        TruckModel tm = new TruckModel();
        tm.setId(1);
        tm.setLicencePlate("same");
        tm.setCrewSize(1);
        tm.setCargoCapacity(1f);
        tm.setCurrentCityId(1);
        
        Truck samePlateTruck = new Truck();
        samePlateTruck.setId(2); //not same id
        samePlateTruck.setLicencePlate("same");
        
        when(truckDaoMock.findByLicensePlate("same"))
            .thenReturn(samePlateTruck);
        when(truckDaoMock.find(1))
        .thenReturn(new Truck());
        
        truckService.editTruck(tm);
    }
    
    /**
     * Test: editTruck
     * Case: edited truck didn't change plate number
     */
    @Test
    public void testEditTruckWhenPlateDidntChange()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        //needed to not fail other checks and validations
        TruckModel tm = new TruckModel();
        tm.setId(1);
        tm.setLicencePlate("same");
        tm.setCrewSize(1);
        tm.setCargoCapacity(1f);
        tm.setCurrentCityId(1);
        
        Truck sameTruck = new Truck();
        sameTruck.setId(1); //not same id
        sameTruck.setLicencePlate("same");
        
        when(truckDaoMock.findByLicensePlate("same"))
            .thenReturn(sameTruck);
        when(truckDaoMock.find(1))
        .thenReturn(sameTruck);
        
        truckService.editTruck(tm);
        
        Mockito.verify(truckDaoMock, times(1)).update(sameTruck);
    }
    
    /**
     * Test: editTruck
     * Case: everything Ok
     */
    @Test
    public void testEditTruckWhenEverythingOk()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        TruckModel tm = new TruckModel();
        tm.setId(1);
        tm.setLicencePlate("test");
        tm.setCrewSize(1);
        tm.setCargoCapacity(1f);
        tm.setCurrentCityId(1);
        
        when(truckDaoMock.find(1))
        .thenReturn(new Truck());
        
        truckService.editTruck(tm);
        
        Mockito.verify(truckDaoMock, times(1)).update(any(Truck.class));
    }
    
    /**
     * Test: editTruck
     * Case: when truck have assigned order
     */
    @Test(expected = ServiceValidationException.class)
    public void testEditTruckWhenTruckHaveAssignedOrder()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        TruckModel tm = new TruckModel();
        tm.setId(1);
        tm.setLicencePlate("test");
        tm.setCrewSize(1);
        tm.setCargoCapacity(1f);
        tm.setCurrentCityId(1);
        
        Truck truck = new Truck();
        truck.setAssignedDeliveryOrder(new DeliveryOrder());
        
        when(truckDaoMock.find(1))
        .thenReturn(truck);
        
        truckService.editTruck(tm);
    }
    
    /**
     * Test: editTruck
     * Case: when truck have invalid plate number
     */
    @Test(expected = ServiceValidationException.class)
    public void testEditTruckWhenPlateValidationFailed()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        TruckModel tm = new TruckModel();
        tm.setId(1);
        tm.setLicencePlate("test");
        tm.setCrewSize(1);
        tm.setCargoCapacity(1f);
        tm.setCurrentCityId(1);
        
        Truck truck = new Truck();
        truck.setAssignedDeliveryOrder(new DeliveryOrder());
        
        when(truckDaoMock.find(1))
        .thenReturn(truck);
        when(plateValidator.validateLicensePlate(any(String.class)))
        .thenReturn(false);
        
        truckService.editTruck(tm);
    }

    /**
     * Test: editTruck
     * Case: when truck not exist
     */
    @Test(expected = ServiceValidationException.class)
    public void testEditTruckWhenTruckNotExist()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        TruckModel tm = new TruckModel();
        tm.setId(1);
        tm.setLicencePlate("test");
        tm.setCrewSize(1);
        tm.setCargoCapacity(1f);
        tm.setCurrentCityId(1);
        
        Truck truck = new Truck();
        truck.setAssignedDeliveryOrder(new DeliveryOrder());
        
        when(truckDaoMock.find(1))
        .thenReturn(null);
        
        truckService.editTruck(tm);
    }
    
    /**
     * Test: addTruck
     * Case: when truck have invalid plate number
     */
    @Test(expected = ServiceValidationException.class)
    public void testAddTruckWhenPlateValidationFailed()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        TruckModel tm = new TruckModel();
        tm.setLicencePlate("test");
        tm.setCrewSize(1);
        tm.setCargoCapacity(1f);
        tm.setCurrentCityId(1);
        
        when(plateValidator.validateLicensePlate(any(String.class)))
        .thenReturn(false);
        
        truckService.addTruck(tm);
    }

    /**
     * Test: editTruck
     * Case: license plate is occupied by another truck
     */
    @Test(expected = ServiceValidationException.class)
    public void testAddTruckWhenLicensePlateOccupied()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        //needed to not fail other checks and validations
        TruckModel tm = new TruckModel();
        tm.setLicencePlate("same");
        tm.setCrewSize(1);
        tm.setCargoCapacity(1f);
        tm.setCurrentCityId(1);
        
        when(truckDaoMock.findByLicensePlate("same"))
            .thenReturn(new Truck()); //truck with same plate
        
        truckService.addTruck(tm);
    }
    
    /**
     * Test: addTruck
     * Case: everything Ok
     */
    @Test
    public void testAddTruckWhenEverythingOk()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        TruckModel tm = new TruckModel();
        tm.setId(1);
        tm.setLicencePlate("test");
        tm.setCrewSize(1);
        tm.setCargoCapacity(1f);
        tm.setCurrentCityId(1);
        
        when(truckDaoMock.find(1))
        .thenReturn(new Truck());
        
        truckService.addTruck(tm);
        
        Mockito.verify(truckDaoMock, times(1)).create(any(Truck.class));
    }
    
    /**
     * Test: removeTruck
     * Case: truck not exist
     */
    @Test(expected = ServiceValidationException.class)
    public void testRemoveTruckWhenTruckNotExist() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        when(truckDaoMock.find(1))
        .thenReturn(null);

        truckService.removeTruck(1);
    }
    
    /**
     * Test: removeTruck
     * Case: truck have assigned order
     */
    @Test(expected = ServiceValidationException.class)
    public void testRemoveTruckWhenTruckHaveAssignedOrder() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        Truck t = new Truck();
        t.setAssignedDeliveryOrder(new DeliveryOrder());
        
        when(truckDaoMock.find(1))
        .thenReturn(t);

        truckService.removeTruck(1);
    }
    
    /**
     * Test: removeTruck
     * Case: truck have assigned drivers
     */
    @Test(expected = ServiceValidationException.class)
    public void testRemoveTruckWhenTruckHaveAssignedDrivers() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        Truck t = new Truck();
        t.setAssignedDeliveryOrder(null);
        t.setDrivers(new HashSet<Driver>(Arrays.asList(new Driver())));
        
        when(truckDaoMock.find(1))
        .thenReturn(t);

        truckService.removeTruck(1);
    }
    
    /**
     * Test: removeAssignedOrderAndDriversFromTruck
     * Case: everything Ok
     */
    @Test
    public void testRemoveTruckWhenEverythinOk() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        Truck t = new Truck();
        t.setAssignedDeliveryOrder(null);
        t.setDrivers(new HashSet<Driver>());
        
        when(truckDaoMock.find(1))
        .thenReturn(t);

        truckService.removeTruck(1);
        Mockito.verify(truckDaoMock, times(1)).delete(t);
    }
    
    /**
     * Test: removeAssignedOrderAndDriversFromTruck
     * Case: truck not exist
     */
    @Test(expected = ServiceValidationException.class)
    public void testRemoveAssignedOrderWhenTruckNotExist() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        when(truckDaoMock.find(1))
        .thenReturn(null);

        truckService.removeAssignedOrderAndDriversFromTruck(1);
    }
    
    /**
     * Test: removeAssignedOrderAndDriversFromTruck
     * Case: there is no order assigned to truck
     */
    @Test(expected = ServiceValidationException.class)
    public void testRemoveAssignedOrderAndDriversFromTruckWhenNoOrder() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        Truck t = new Truck();
        t.setAssignedDeliveryOrder(null);
        
        when(truckDaoMock.find(1))
        .thenReturn(t);

        truckService.removeAssignedOrderAndDriversFromTruck(1);
    }
    
    /**
     * Test: removeAssignedOrderAndDriversFromTruck
     * Case: order have READY_TO_GO status -- removal is forbiden
     */
    @Test(expected = ServiceValidationException.class)
    public void testRemoveAssignedOrderWhenOrderStatusWrong() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        Truck t = new Truck();
        DeliveryOrder o = new DeliveryOrder();
        o.setStatus(OrderStatus.READY_TO_GO);
        t.setAssignedDeliveryOrder(o);
        
        when(truckDaoMock.find(1))
        .thenReturn(t);

        truckService.removeAssignedOrderAndDriversFromTruck(1);
    }
    
    /**
     * Test: removeAssignedOrderAndDriversFromTruck
     * Case: everything Ok
     */
    @Test
    public void testRemoveAssignedOrderWhenEvyrithingOk() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        mockSetup();
        TrucksService truckService = new TrucksSeviceImpl(truckDaoMock, plateValidator);
        
        Truck t = new Truck();
        t.setAssignedDeliveryOrder(new DeliveryOrder());
        t.setDrivers(new HashSet<Driver>());
        
        when(truckDaoMock.find(1))
        .thenReturn(t);

        truckService.removeAssignedOrderAndDriversFromTruck(1);
        Mockito.verify(truckDaoMock, times(1)).update(t);
    }
    
    
}

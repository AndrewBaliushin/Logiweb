package com.tsystems.javaschool.logiweb.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import com.tsystems.javaschool.logiweb.dao.CargoDao;
import com.tsystems.javaschool.logiweb.dao.CityDao;
import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.City;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.service.CargoService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

public class CargoServiceImplTest {

    private CargoDao cargoDaoMock;
    private CityDao cityDaoMock;
    private DeliveryOrderDao deliveryOrderDaoMock;

    private void setupMocks() {
        cargoDaoMock = mock(CargoDao.class);
        cityDaoMock = mock(CityDao.class);
        deliveryOrderDaoMock = mock(DeliveryOrderDao.class);        
    }
    
    private Cargo createValidTestCargo() {
        Cargo newCargo = new Cargo();
        
        City city1 = new City();
        city1.setId(1);
        City city2 = new City();
        city2.setId(2);
        
        DeliveryOrder order = new DeliveryOrder();
        order.setStatus(OrderStatus.NOT_READY);
        
        newCargo.setTitle("test");
        newCargo.setWeight(1f);
        newCargo.setOriginCity(city1);
        newCargo.setDestinationCity(city2);
        newCargo.setOrderForThisCargo(order);
        
        return newCargo;
    }
    
    /*
     * addCargo method works in such way
     * that it checks that cities and order ids are valid.
     * We return same objects for dao calls so validation 
     * could be successful.
     */
    private void setMocksToPassInnerValidationInAddCargo(Cargo sourceCargo)
            throws DaoException {
      //needed for inner irrelevant validations
        when(cityDaoMock.find(sourceCargo.getOriginCity().getId()))
            .thenReturn(sourceCargo.getOriginCity());
        when(cityDaoMock.find(sourceCargo.getDestinationCity().getId()))
            .thenReturn(sourceCargo.getDestinationCity());
        when(deliveryOrderDaoMock.find(sourceCargo.getOrderForThisCargo().getId()))
            .thenReturn(sourceCargo.getOrderForThisCargo());
    }
    
    @Test
    public void testAddCargoWhenEverythingOk()
            throws ServiceValidationException, LogiwebServiceException, DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        Cargo newCargo = createValidTestCargo();
        
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        cargoService.addCargo(newCargo);
        
        Mockito.verify(cargoDaoMock, times(1)).create(newCargo);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenTitleIsBlank()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        Cargo newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        newCargo.setTitle(null);
        
        cargoService.addCargo(newCargo);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenOriginCityMissing() 
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        Cargo newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        newCargo.setOriginCity(null);
        
        cargoService.addCargo(newCargo);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenDestinationSityMissing()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        Cargo newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        newCargo.setDestinationCity(null);
        
        cargoService.addCargo(newCargo);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenCitiesAreSame() 
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        Cargo newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        newCargo.setDestinationCity(newCargo.getOriginCity());
        
        cargoService.addCargo(newCargo);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenOrderInWrongState() 
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        Cargo newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        newCargo.getOrderForThisCargo().setStatus(OrderStatus.READY_TO_GO);
        
        cargoService.addCargo(newCargo);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenOrderHaveAssignedTruck() 
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        Cargo newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        newCargo.getOrderForThisCargo().setAssignedTruck(new Truck());
        
        cargoService.addCargo(newCargo);
    }
}

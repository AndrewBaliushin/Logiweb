package com.tsystems.javaschool.logiweb.service.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import org.hibernate.annotations.Any;
import org.junit.Before;
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
import com.tsystems.javaschool.logiweb.model.CargoModel;
import com.tsystems.javaschool.logiweb.service.CargoService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

public class CargoServiceImplTest {

    private CargoDao cargoDaoMock;
    private CityDao cityDaoMock;
    private DeliveryOrderDao deliveryOrderDaoMock;
    
    private CargoService cargoService;

    @Before
    public void setupMocks() {
        cargoDaoMock = mock(CargoDao.class);
        cityDaoMock = mock(CityDao.class);
        deliveryOrderDaoMock = mock(DeliveryOrderDao.class);    
        
        cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
    }
    
    private CargoModel createValidTestCargo() {
        CargoModel newCargo = new CargoModel();
        
        City city1 = new City();
        city1.setId(1);
        City city2 = new City();
        city2.setId(2);
        
        newCargo.setTitle("test");
        newCargo.setWeight(1f);
        newCargo.setOriginCityId(1);
        newCargo.setDestinationCityId(2);
        newCargo.setOrderIdForThisCargo(1);
        
        return newCargo;
    }
    
    /*
     * addCargo method works in such way
     * that it checks that cities and order ids are valid.
     * We return same objects for dao calls so validation 
     * could be successful.
     */
    private void setMocksToPassInnerValidationInAddCargo(CargoModel sourceCargo)
            throws DaoException {
        City originCity = new City();
        originCity.setId(sourceCargo.getOriginCityId());
        
        City destCity = new City();
        destCity.setId(sourceCargo.getDestinationCityId());
        
        DeliveryOrder order = new DeliveryOrder();
        order.setId(sourceCargo.getOrderIdForThisCargo());
        order.setStatus(OrderStatus.NOT_READY);
        
      //needed for inner irrelevant validations
        when(cityDaoMock.find(sourceCargo.getOriginCityId()))
            .thenReturn(originCity);
        when(cityDaoMock.find(sourceCargo.getDestinationCityId()))
            .thenReturn(destCity);
        when(deliveryOrderDaoMock.find(sourceCargo.getOrderIdForThisCargo()))
            .thenReturn(order);
    }
    
    @Test
    public void testAddCargoWhenEverythingOk()
            throws ServiceValidationException, LogiwebServiceException, DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        CargoModel newCargo = createValidTestCargo();
        
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        cargoService.addCargo(newCargo);
        
        Mockito.verify(cargoDaoMock, times(1)).create(Mockito.any(Cargo.class));
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenTitleIsBlank()
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        CargoModel newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        newCargo.setTitle(null);
        
        cargoService.addCargo(newCargo);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenCitiesAreSame() 
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        CargoModel newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);        
        
        newCargo.setDestinationCityId(newCargo.getOriginCityId());
        City sameCity = new City();
        when(cityDaoMock.find(newCargo.getOriginCityId())).thenReturn(
                sameCity);
        when(cityDaoMock.find(newCargo.getDestinationCityId())).thenReturn(
                sameCity);
        
        cargoService.addCargo(newCargo);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenOrderInWrongState() 
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        CargoModel newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        DeliveryOrder wrongStatusOrder = new DeliveryOrder();
        wrongStatusOrder.setStatus(OrderStatus.READY_TO_GO);
        
        when(deliveryOrderDaoMock.find(newCargo.getOrderIdForThisCargo()))
            .thenReturn(wrongStatusOrder);
        
        cargoService.addCargo(newCargo);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAddCargoWhenOrderHaveAssignedTruck() 
            throws ServiceValidationException, LogiwebServiceException,
            DaoException {
        setupMocks();
        CargoService cargoService = new CargoServiceImpl(cargoDaoMock,
                cityDaoMock, deliveryOrderDaoMock);
        CargoModel newCargo = createValidTestCargo();
        setMocksToPassInnerValidationInAddCargo(newCargo);
        
        DeliveryOrder wrongStatusOrder = new DeliveryOrder();
        wrongStatusOrder.setStatus(OrderStatus.NOT_READY);
        wrongStatusOrder.setAssignedTruck(new Truck());
        
        when(deliveryOrderDaoMock.find(newCargo.getOrderIdForThisCargo()))
            .thenReturn(wrongStatusOrder);
        
        cargoService.addCargo(newCargo);
    }
}

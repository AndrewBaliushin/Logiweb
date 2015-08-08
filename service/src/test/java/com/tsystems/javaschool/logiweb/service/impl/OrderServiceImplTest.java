package com.tsystems.javaschool.logiweb.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.tsystems.javaschool.logiweb.dao.DeliveryOrderDao;
import com.tsystems.javaschool.logiweb.dao.TruckDao;
import com.tsystems.javaschool.logiweb.dao.exceptions.DaoException;
import com.tsystems.javaschool.logiweb.entities.Cargo;
import com.tsystems.javaschool.logiweb.entities.DeliveryOrder;
import com.tsystems.javaschool.logiweb.entities.Driver;
import com.tsystems.javaschool.logiweb.entities.Truck;
import com.tsystems.javaschool.logiweb.entities.status.CargoStatus;
import com.tsystems.javaschool.logiweb.entities.status.OrderStatus;
import com.tsystems.javaschool.logiweb.entities.status.TruckStatus;
import com.tsystems.javaschool.logiweb.service.OrderService;
import com.tsystems.javaschool.logiweb.service.exceptions.LogiwebServiceException;
import com.tsystems.javaschool.logiweb.service.exceptions.ServiceValidationException;

public class OrderServiceImplTest {
    
    private DeliveryOrderDao deliveryOrderDaoMock;
    private TruckDao truckDaoMock;
    
    private void setupMocks() {
        deliveryOrderDaoMock = Mockito.mock(DeliveryOrderDao.class);
        truckDaoMock = Mockito.mock(TruckDao.class);
    }

    @Test
    public void testAssignTruckToOrderWhenEverythingOk() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        Truck truck = new Truck();
        truck.setId(1);
        truck.setStatus(TruckStatus.OK);
        DeliveryOrder order = new DeliveryOrder();
        order.setAssignedCargoes(new HashSet<Cargo>(Arrays.asList(new Cargo())));

        when(truckDaoMock.find(1))
            .thenReturn(truck);
        when(deliveryOrderDaoMock.find(1))
            .thenReturn(order);
        
        orderService.assignTruckToOrder(1, 1);
        
        Mockito.verify(truckDaoMock, times(1)).update(truck);
        Mockito.verify(deliveryOrderDaoMock, times(1)).update(order);
    }

    @Test(expected = ServiceValidationException.class)
    public void testAssignTruckToOrderWhenNotExist() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        when(truckDaoMock.find(1))
            .thenReturn(null);
        
        orderService.assignTruckToOrder(1, 1);
    }

    @Test(expected = ServiceValidationException.class)
    public void testAssignTruckToOrderWhenTruckIsNotOk() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        Truck truck = new Truck();
        truck.setId(1);
        truck.setStatus(TruckStatus.FAULTY);
        DeliveryOrder order = new DeliveryOrder();
        order.setAssignedCargoes(new HashSet<Cargo>(Arrays.asList(new Cargo())));

        when(truckDaoMock.find(1))
            .thenReturn(truck);
        when(deliveryOrderDaoMock.find(1))
            .thenReturn(order);
        
        orderService.assignTruckToOrder(1, 1);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAssignTruckToOrderWhenTruckAlreadyHaveOrder() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        Truck truck = new Truck();
        truck.setId(1);
        truck.setStatus(TruckStatus.OK);
        
        truck.setAssignedDeliveryOrder(new DeliveryOrder());
        
        DeliveryOrder order = new DeliveryOrder();
        order.setAssignedCargoes(new HashSet<Cargo>(Arrays.asList(new Cargo())));

        when(truckDaoMock.find(1))
            .thenReturn(truck);
        when(deliveryOrderDaoMock.find(1))
            .thenReturn(order);
        
        orderService.assignTruckToOrder(1, 1);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAssignTruckToOrderWhenOrderNotExist() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        Truck truck = new Truck();
        truck.setId(1);
        truck.setStatus(TruckStatus.OK);
        
        when(truckDaoMock.find(1))
            .thenReturn(truck);
        when(deliveryOrderDaoMock.find(1))
            .thenReturn(null);
        
        orderService.assignTruckToOrder(1, 1);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAssignTruckToOrderWhenOrderAlreadyHaveTruck() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        Truck truck = new Truck();
        truck.setId(1);
        truck.setStatus(TruckStatus.OK);
        DeliveryOrder order = new DeliveryOrder();
        order.setAssignedCargoes(new HashSet<Cargo>(Arrays.asList(new Cargo())));
        order.setAssignedTruck(new Truck());

        when(truckDaoMock.find(1))
            .thenReturn(truck);
        when(deliveryOrderDaoMock.find(1))
            .thenReturn(order);
        
        orderService.assignTruckToOrder(1, 1);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testAssignTruckToOrderWhenNoCargoesInOrder() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        Truck truck = new Truck();
        truck.setId(1);
        truck.setStatus(TruckStatus.OK);
        DeliveryOrder order = new DeliveryOrder();
        
        order.setAssignedCargoes(new HashSet<Cargo>());

        when(truckDaoMock.find(1))
            .thenReturn(truck);
        when(deliveryOrderDaoMock.find(1))
            .thenReturn(order);
        
        orderService.assignTruckToOrder(1, 1);
    }
    
    private DeliveryOrder createValidTestOrder() {
        Truck truck = new Truck();
        truck.setCrewSize(1);
        truck.setDrivers(new HashSet<Driver>(Arrays.asList(new Driver())));
        
        DeliveryOrder order = new DeliveryOrder();
        order.setAssignedCargoes(new HashSet<Cargo>(Arrays.asList(new Cargo())));
        order.setStatus(OrderStatus.NOT_READY);
        order.setAssignedTruck(truck);
        
        return order;
    }
    
    @Test
    public void testSetReadyStatusForOrderWhenEverythingOk() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(order);
        
        orderService.setReadyStatusForOrder(order.getId());
        Mockito.verify(deliveryOrderDaoMock, times(1)).update(order);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testSetReadyStatusForOrderWhenOrderNotExist() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(null);
        
        orderService.setReadyStatusForOrder(order.getId());
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testSetReadyStatusForOrderWhenOrderIsEpmpty() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        order.setAssignedCargoes(new HashSet<Cargo>());
        
        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(order);
        
        orderService.setReadyStatusForOrder(order.getId());
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testSetReadyStatusForOrderWhenTruckNotAssigned() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        order.setAssignedTruck(null);
        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(order);
        
        orderService.setReadyStatusForOrder(order.getId());
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testSetReadyStatusForOrderWhenNotEnoughDrivers() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        int currentCrewSize = order.getAssignedTruck().getDrivers().size();
        order.getAssignedTruck().setCrewSize(currentCrewSize + 1);
        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(order);
        
        orderService.setReadyStatusForOrder(order.getId());
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testSetReadyStatusForOrderWhenOrderHaveWrongStatus() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        order.setStatus(OrderStatus.DELIVERED);
        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(order);
        
        orderService.setReadyStatusForOrder(order.getId());
    }
    
    @Test
    public void testIsAllCargoesInOrderDeliveredWhenPositive() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        
        Cargo cargo1 = new Cargo();
        cargo1.setStatus(CargoStatus.DELIVERED);
        Cargo cargo2 = new Cargo();
        cargo2.setStatus(CargoStatus.DELIVERED);

        order.setAssignedCargoes(new HashSet<Cargo>(Arrays.asList(cargo1,
                cargo2)));

        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(order);

        Assert.assertTrue(orderService.isAllCargoesInOrderDelivered(order
                .getId()));
    }
    
    @Test
    public void testIsAllCargoesInOrderDeliveredWhenNegative() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        
        Cargo cargo1 = new Cargo();
        cargo1.setStatus(CargoStatus.DELIVERED);
        Cargo cargo2 = new Cargo();
        cargo2.setStatus(CargoStatus.PICKED_UP);

        order.setAssignedCargoes(new HashSet<Cargo>(Arrays.asList(cargo1,
                cargo2)));

        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(order);

        Assert.assertFalse(orderService.isAllCargoesInOrderDelivered(order
                .getId()));
    }
    
    @Test
    public void testSetStatusDeliveredForOrderWhenEverythingOk() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        Cargo cargo = new Cargo();
        cargo.setStatus(CargoStatus.DELIVERED);
        order.setAssignedCargoes(new HashSet<Cargo>(Arrays.asList(cargo)));
        
        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(order);
        
        orderService.setStatusDeliveredForOrder(order.getId());
        Mockito.verify(deliveryOrderDaoMock, times(1)).update(order);
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testSetStatusDeliveredForOrderWhenOrderNotExist() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(null);
        
        orderService.setStatusDeliveredForOrder(order.getId());
    }
    
    @Test(expected = ServiceValidationException.class)
    public void testSetStatusDeliveredForOrderWhenNotAllCargoDelivered() throws ServiceValidationException,
            LogiwebServiceException, DaoException {
        setupMocks();
        OrderService orderService = new OrderServiceImpl(deliveryOrderDaoMock,
                truckDaoMock);
        
        DeliveryOrder order = createValidTestOrder();
        
        Cargo cargo1 = new Cargo();
        cargo1.setStatus(CargoStatus.DELIVERED);
        Cargo cargo2 = new Cargo();
        cargo2.setStatus(CargoStatus.PICKED_UP);

        order.setAssignedCargoes(new HashSet<Cargo>(Arrays.asList(cargo1,
                cargo2)));
        
        when(deliveryOrderDaoMock.find(order.getId()))
            .thenReturn(order);
        
        orderService.setStatusDeliveredForOrder(order.getId());
    }
}

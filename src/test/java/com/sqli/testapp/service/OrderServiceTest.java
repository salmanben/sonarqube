package com.sqli.testapp.service;

import com.sqli.testapp.dto.OrderDto;
import com.sqli.testapp.mapper.OrderMapper;
import com.sqli.testapp.model.Client;
import com.sqli.testapp.model.Order;
import com.sqli.testapp.repository.ClientRepository;
import com.sqli.testapp.repository.OrderRepository;
import com.sqli.testapp.service.imp.OrderServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private OrderServiceImp orderService;

    @Test
    void testGetAllOrders() {
        List<Order> orders = List.of(new Order(), new Order(), new Order());
        when(orderRepository.findAll()).thenReturn(orders);
        assertEquals(3, orderService.getAllOrders().size());
    }

    @Test
    void testGetOrderById() {
        int id = 1;
        Order orderMock = new Order();
        when(orderRepository.findById(id)).thenReturn(Optional.of(orderMock));
        assertNotNull(orderService.getOrderById(id));
    }

    @Test
    void testGetOrderByIdNotFound() {
        int id = 1;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        assertNull(orderService.getOrderById(id));
    }

    @Test
    void testStoreOrder() {
        OrderDto orderDto = new OrderDto(1, 100.0f, 1);
        Client client = new Client();
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        Order order = new Order();
        when(orderMapper.dtoRequestToEntity(orderDto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.storeOrder(orderDto);
        assertNotNull(result);
    }

    @Test
    void testStoreOrderClientNotFound() {
        OrderDto orderDto = new OrderDto(1, 100.0f, 1);
        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.storeOrder(orderDto);
        });
        assertEquals("Client not found with id: 1", exception.getMessage());
    }

    @Test
    void testUpdateOrder() {
        int id = 1;
        OrderDto orderDto = new OrderDto(1, 100.0f, 1);
        Client client = new Client();
        Order existingOrder = new Order(1, 200f, client);
        existingOrder.setAmount(orderDto.getAmount());
        existingOrder.setClient(client);

        when(orderRepository.findById(id)).thenReturn(Optional.of(existingOrder));
        when(clientRepository.findById(orderDto.getClientId())).thenReturn(Optional.of(client));
        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);

        Order result = orderService.updateOrder(id, orderDto);
        assertNotNull(result);
    }

    @Test
    void testUpdateOrderClientNotFound() {
        int id = 1;
        OrderDto orderDto = new OrderDto(1, 100.0f, 1);
        when(orderRepository.findById(id)).thenReturn(Optional.of(new Order()));
        when(clientRepository.findById(orderDto.getClientId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrder(id, orderDto);
        });
        assertEquals("Client not found with id: 1", exception.getMessage());
    }

    @Test
    void testUpdateOrderNotFound() {
        int id = 1;
        OrderDto orderDto = new OrderDto(1, 100.0f, 1);
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrder(id, orderDto);
        });
        assertEquals("Order not found with id: 1", exception.getMessage());
    }

    @Test
    void testRemoveOrder() {
        int id = 1;
        doNothing().when(orderRepository).deleteById(id);

        orderService.removeOrder(id);

        verify(orderRepository, times(1)).deleteById(id);
    }
}

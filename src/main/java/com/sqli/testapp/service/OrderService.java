package com.sqli.testapp.service;

import com.sqli.testapp.dto.OrderDto;
import com.sqli.testapp.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    Order getOrderById(int id);
    Order storeOrder(OrderDto orderDto);
    Order updateOrder(int id, OrderDto orderDto);
    boolean removeOrder(int id);

}

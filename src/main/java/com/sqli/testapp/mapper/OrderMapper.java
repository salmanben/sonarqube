package com.sqli.testapp.mapper;

import com.sqli.testapp.dto.OrderDto;
import com.sqli.testapp.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order dtoRequestToEntity(OrderDto orderDto){
        Order order = new Order();
        order.setAmount(orderDto.getAmount());
        return  order;
    }
}

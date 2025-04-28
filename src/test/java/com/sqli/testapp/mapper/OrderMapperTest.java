package com.sqli.testapp.mapper;

import com.sqli.testapp.dto.OrderDto;
import com.sqli.testapp.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderMapperTest {
    private final OrderMapper mapper = new OrderMapper();

    @Test
    public void testDtoRequestToEntity(){
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(12f);

        Order order = mapper.dtoRequestToEntity(orderDto);

        Assertions.assertNotNull(order);
        Assertions.assertEquals(order.getAmount(), orderDto.getAmount());
    }
}

package com.sqli.testapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqli.testapp.model.Client;
import com.sqli.testapp.model.Order;
import com.sqli.testapp.service.imp.OrderServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderServiceImp orderServiceImp;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllOrders() throws Exception {
        Order order = new Order();
        Client client = new Client();
        client.setId(1);
        order.setAmount(18f);
        order.setCreatedAt(LocalDate.now());
        order.setUpdatedAt(LocalDate.now());
        order.setClient(client);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderServiceImp.getAllOrders()).thenReturn(orders);

        ResultActions response = mockMvc.perform(get("/api/v1/orders"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(orders.size())))
                .andExpect(jsonPath("$[0].amount", is(orders.get(0).getAmount())));
    }
}

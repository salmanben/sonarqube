package com.sqli.testapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqli.testapp.dto.OrderDto;
import com.sqli.testapp.model.Client;
import com.sqli.testapp.model.Order;
import com.sqli.testapp.model.Role;
import com.sqli.testapp.service.imp.OrderServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private OrderServiceImp orderService;

    @Test
    public void testGetAll() throws Exception {
        Order order1 = new Order(1, 17f, new Client());
        List<Order> orders = new ArrayList<>();
        orders.add(order1);

        when(orderService.getAllOrders()).thenReturn(orders);

        ResultActions response = mockMvc.perform(get("/api/v1/orders"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(orders.size())));
    }

    @Test
    public void testGetById() throws Exception {
        Order order = new Order(1, 17f, new Client());

        when(orderService.getOrderById(order.getId())).thenReturn(order);

        ResultActions response = mockMvc.perform(get("/api/v1/orders/" + order.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order.getId())))
                .andExpect(jsonPath("$.amount", is(Double.valueOf(order.getAmount()))));
    }

    @Test
    public void testGetByIdNotFound() throws Exception {
        int notExistingId = 1000000;

        when(orderService.getOrderById(notExistingId)).thenReturn(null);

        ResultActions response = mockMvc.perform(get("/api/v1/orders/" + notExistingId));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void testStore() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(17f);
        Client client = new Client(1, "Salman", "salman@example.com", "12345", 28, Role.CLIENT);
        orderDto.setClientId(client.getId());
        Order savedOrder = new Order(1, orderDto.getAmount(), client, LocalDate.now(), LocalDate.now());

        when(orderService.storeOrder(orderDto)).thenReturn(savedOrder);

        ResultActions response = mockMvc.perform(post("/api/v1/orders")
                .content(objectMapper.writeValueAsString(orderDto))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message.id", is(savedOrder.getId())));
    }

    @Test
    public void testStoreClientNotFound() throws Exception {
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(17f);
        orderDto.setClientId(220000);

        when(orderService.storeOrder(orderDto)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(post("/api/v1/orders")
                .content(objectMapper.writeValueAsString(orderDto))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("error")));
    }

    @Test
    public void testUpdate() throws Exception {
        Client client = new Client(1, "Salman", "salman@example.com", "12345", 28, Role.CLIENT);
        OrderDto orderDto = new OrderDto();
        orderDto.setAmount(17f);
        orderDto.setClientId(client.getId());
        Order updatedOrder = new Order(1, orderDto.getAmount(), client, LocalDate.of(2025, 04, 26), LocalDate.now());

        when(orderService.updateOrder(client.getId(), orderDto)).thenReturn(updatedOrder);

        ResultActions response = mockMvc.perform(put("/api/v1/orders/" + client.getId())
                .content(objectMapper.writeValueAsString(orderDto))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message.id", is(updatedOrder.getId())));
    }

    @Test
    public void testRemove() throws Exception {
        int id = 1;
        when(orderService.removeOrder(id)).thenReturn(true);

        ResultActions response = mockMvc.perform(delete("/api/v1/orders/" + id));

        response.andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        int notExistingId = 100000000;

        when(orderService.removeOrder(notExistingId)).thenReturn(false);

        ResultActions response = mockMvc.perform(delete("/api/v1/orders/" + notExistingId));

        response.andExpect(status().isNotFound());
    }
}

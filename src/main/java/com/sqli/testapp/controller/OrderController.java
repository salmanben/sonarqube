package com.sqli.testapp.controller;

import com.sqli.testapp.dto.OrderDto;
import com.sqli.testapp.model.Order;
import com.sqli.testapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("")
    public List<Order> getAll() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable int id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/store")
    public ResponseEntity<Map<String, Object>> store(@Valid @RequestBody OrderDto orderDto, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            response.put("status", "error");
            response.put("message", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Order savedOrder = orderService.storeOrder(orderDto);
        response.put("status", "success");
        response.put("message", savedOrder);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody OrderDto orderDto, BindingResult bindingResult,
                                                      @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            response.put("status", "error");
            response.put("message", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Order updatedOrder = orderService.updateOrder(id, orderDto);
        response.put("status", "success");
        response.put("message", updatedOrder);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        orderService.removeOrder(id);
    }
}

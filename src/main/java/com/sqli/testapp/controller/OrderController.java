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

    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String ERROR = "error";
    private static final String SUCCESS = "success";
    private static final String ORDER_DELETED_SUCCESSFULLY = "Order deleted successfully.";
    private static final String ORDER_NOT_FOUND = "Order not found.";

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable int id) {
        Order order =  orderService.getOrderById(id);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> store(@Valid @RequestBody OrderDto orderDto, BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            response.put(STATUS, ERROR);
            response.put(MESSAGE, errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Order savedOrder = orderService.storeOrder(orderDto);
        response.put(STATUS, SUCCESS);
        response.put(MESSAGE, savedOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody OrderDto orderDto, BindingResult bindingResult,
                                                      @PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList());
            response.put(STATUS, ERROR);
            response.put(MESSAGE, errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Order updatedOrder = orderService.updateOrder(id, orderDto);
        response.put(STATUS, SUCCESS);
        response.put(MESSAGE, updatedOrder);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable int id) {
        boolean isDeleted = orderService.removeOrder(id);

        Map<String, Object> response = new HashMap<>();
        if (isDeleted) {
            response.put(STATUS, SUCCESS);
            response.put(MESSAGE, ORDER_DELETED_SUCCESSFULLY);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } else {
            response.put(STATUS, ERROR);
            response.put(MESSAGE, ORDER_NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

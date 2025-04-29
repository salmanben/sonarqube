package com.sqli.testapp.service.imp;

import com.sqli.testapp.dto.OrderDto;
import com.sqli.testapp.mapper.OrderMapper;
import com.sqli.testapp.model.Client;
import com.sqli.testapp.model.Order;
import com.sqli.testapp.repository.ClientRepository;
import com.sqli.testapp.repository.OrderRepository;
import com.sqli.testapp.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ClientRepository clientRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order storeOrder(OrderDto orderDto) {
        Optional<Client> clientOptional = clientRepository.findById(orderDto.getClientId());
        if (!clientOptional.isPresent()) {
            throw new EntityNotFoundException("Client not found with id: " + orderDto.getClientId());
        }
        Order order = orderMapper.dtoRequestToEntity(orderDto);
        order.setClient(clientOptional.get());
        order = orderRepository.save(order);
        return order;
    }

    @Override
    public Order updateOrder(int id, OrderDto orderDto) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(!orderOptional.isPresent()){
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        Optional<Client> clientOptional = clientRepository.findById(orderDto.getClientId());
        if (!clientOptional.isPresent()) {
            throw new EntityNotFoundException("Client not found with id: " + orderDto.getClientId());
        }
        Order order = orderOptional.get();
        order.setAmount(orderDto.getAmount());
        order.setClient(clientOptional.get());
        order = orderRepository.save(order);
        return order;
    }

    @Override
    public boolean removeOrder(int id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            return false;
        }
        orderRepository.deleteById(id);
        return true;
    }


}

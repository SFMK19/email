package com.surf0335.AI_Recommendation_System.services;

import com.surf0335.AI_Recommendation_System.model.Order;
import com.surf0335.AI_Recommendation_System.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersById(int id) {
        return orderRepository.findAllByUserId(id);
    }

    public Order saveOrders(Order order) { return orderRepository.save(order); }
}

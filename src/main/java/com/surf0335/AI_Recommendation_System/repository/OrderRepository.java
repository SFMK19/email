package com.surf0335.AI_Recommendation_System.repository;
import com.surf0335.AI_Recommendation_System.model.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByUserId(int userId);
    List<Order> findAllByUserId(int userId);
}


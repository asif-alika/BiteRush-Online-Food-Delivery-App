package com.ty.BiteRush.repository;

import com.ty.BiteRush.entity.Order;
import com.ty.BiteRush.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}

package com.ty.BiteRush.repository;
import com.ty.BiteRush.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderRepository extends JpaRepository<Order, Long> {}

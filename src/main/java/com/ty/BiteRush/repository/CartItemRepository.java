package com.ty.BiteRush.repository;
import com.ty.BiteRush.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartItemRepository extends JpaRepository<CartItem, Long> {}

package com.ty.BiteRush.service;

import com.ty.BiteRush.entity.Order;
import com.ty.BiteRush.entity.User;

public interface OrderService {
  Order checkout(User user, String a1, String a2, String city, String state, String zip);
  void markAsPaid(Long orderId, String paymentId);
}

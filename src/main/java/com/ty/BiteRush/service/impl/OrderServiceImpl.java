package com.ty.BiteRush.service.impl;
import com.ty.BiteRush.entity.*;
import com.ty.BiteRush.repository.OrderRepository;
import com.ty.BiteRush.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service @RequiredArgsConstructor @Transactional
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepo;

  @Override
  public Order checkout(User user, String a1, String a2, String city, String state, String zip){
    Cart cart = user.getCart();
    if(cart==null || cart.getItems().isEmpty()) throw new IllegalStateException("Cart is empty");
    Order order = Order.builder()
      .user(user)
      .createdAt(LocalDateTime.now())
      .addressLine1(a1).addressLine2(a2).city(city).state(state).zip(zip)
      .build();
    double total = 0;
    for(CartItem ci: cart.getItems()){
      OrderItem oi = OrderItem.builder()
        .order(order).foodItem(ci.getFoodItem())
        .quantity(ci.getQuantity())
        .priceAtPurchase(ci.getFoodItem().getPrice())
        .build();
      order.getItems().add(oi);
      total += ci.getQuantity()*ci.getFoodItem().getPrice();
    }
    order.setTotal(total);
    orderRepo.save(order);
    cart.getItems().clear();
    return order;
  }
}

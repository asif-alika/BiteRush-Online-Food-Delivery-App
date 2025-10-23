package com.ty.BiteRush.service;
import com.ty.BiteRush.entity.*;
public interface CartService {
  Cart getActiveCart(User user);
  void addToCart(User user, Long foodId, int qty);
  void updateItem(User user, Long cartItemId, int qty);
  void removeItem(User user, Long cartItemId);
  double cartTotal(Cart cart);
  void clear(Cart cart);
}
